package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //상속관계이므로 지정 필요(싱글테이블은 한테이블에 모든 값들을 저장하는 방식)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    //안에서 자체적으로 해결하는 방식을 만드는 것이 가장 좋다

    /**
     * 재고 증가(stock)
     */
    public void addStock(int quantity){
        this.stockQuantity+=quantity;
    }

    /**
     * 재고 감소(stock)
     */
    public void removeStock(int quantity)  {
        int restStock=this.stockQuantity-quantity;
        if(restStock<0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity=restStock;
    }

}
