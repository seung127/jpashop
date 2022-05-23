package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {


    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model){
        model.addAttribute("form",new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form){

        Book book=new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);
        return "redirect:/items";
    }

    @GetMapping("/items")
    public String list(Model model){
        List<Item> items=itemService.findItems();
        model.addAttribute("items",items);
        return "items/itemList";
    }

    @GetMapping(value = "/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId")Long itemId,Model model){
        Book item = (Book) itemService.findOne(itemId);

        BookForm form=new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form",form);
        return "items/updateItemForm";
    }

    @PostMapping(value= "/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form") BookForm form) {

        Book book=new Book();
        book.setId(form.getId()); //id값이 들어가므로 jpa에 한번 들어갔다온 것 ('준영속 상태'의 객체라고 한다)  <-- 영속성 컨텍스트가 더는 관리 하지 않는 엔티티
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);  //em.merge()가 된다
        return "redirect:/items";
    }

    /**준영속 엔티티 수정하는 가지 방법
     * 1.변경 감지 기능 사용 : 원래대로의 사용법(선택적으로 수정)
     * 2.병합 (merge)사용 : 준영속상태를 영속상태로 만드는 것(모든 걸 수정해야함-설정을 안하면 null상태가 되어버리는 문제가 발생) 그래서 사용하지 않는 것이 좋다
     *
     * 좋은 방식
     * updateItem을 만들어서
     * itemservice.updateItem(id,form.getName()...)
     */
}
