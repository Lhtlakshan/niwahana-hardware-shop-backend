package org.icet.crm.service.admin.coupon.impl;

import lombok.RequiredArgsConstructor;
import org.icet.crm.entity.Coupon;
import org.icet.crm.exceptions.ValidationException;
import org.icet.crm.repository.CouponRepository;
import org.icet.crm.service.admin.coupon.AdminCouponService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCouponServiceImpl implements AdminCouponService {

    private final CouponRepository couponRepository;

    public Coupon createCoupon(Coupon coupon){
        if(couponRepository.existsByCode(coupon.getCode())){
            throw new ValidationException("Coupon code already exists");
        }
        return couponRepository.save(coupon);
    }

    public List<Coupon> getAllCoupons(){
        return couponRepository.findAll();
    }
}
