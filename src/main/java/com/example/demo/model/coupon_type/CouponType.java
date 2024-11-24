package com.example.demo.model.coupon_type;

import com.example.demo.model.Cart;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,    // Use logical names to distinguish types
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CartWise.class, name = "cart-wise"),
        @JsonSubTypes.Type(value = ProductWise.class, name = "product-wise"),
        @JsonSubTypes.Type(value = BxGy.class, name = "bxgy")
})
public interface CouponType {
    public String getName();
    public boolean isApplicable(Cart cart);
    public double apply(Cart cart);
    public String discountDetails();
}
