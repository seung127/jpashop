package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")  //값을 수정할 때 서로 연관된 상황에서 어떻게 해야하는가? 누가 업데이트를 해야하는 것인가? 보통 다쪽으로 설정하는 것이 좋다
    private Member member;

    @OneToMany(mappedBy = "order" ,cascade=CascadeType.ALL) //persist를 한번에 처리
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문 상태[ORDER , CANCEL]


    //==연관관계 편의 메서드===// many가 가지고 있는 것이 좋다
    public void setMember(Member member){ //본인도 멤버를 받고 연관된 member에는 this로 값을 준다 / 양방향 편의 메서드를 만들어주는 것
        this.member=member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void SetDelibery(Delivery delivery){
        this.delivery=delivery;
        delivery.setOrder(this);
    }


    //==생성 메서드==//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        Order order=new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }

        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }


    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel(){
        if(delivery.getStatus()==DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();  //orderitem클래스에 cancel 을 만들어서 재고를 원래대고 돌려놓는 것을 만든다
        }
    }

    //==조회 로직==//

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
        /**
        int totalPrice=0;
        for (OrderItem orderItem : orderItems) {
            totalPrice+=orderItem.getTotalPrice();  //orderitem내에서 전체 값 계산 클래스 이미 만들어 놓았다
        }
        return totalPrice;**/
        int totalPrice= orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        return totalPrice;

    }

}
