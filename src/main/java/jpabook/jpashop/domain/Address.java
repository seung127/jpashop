package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

//값 타입은 변경이 안되게 만들어야 한다. 생성할 때만 하고 setter제공 안 하는 것이 좋은 설계
@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;


    //기본 생성자만 만들자
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
