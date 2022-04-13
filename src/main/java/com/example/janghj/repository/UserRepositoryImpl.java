//package com.example.janghj.repository;
//
//import com.example.janghj.repository.dto.QUserOrderDto;
//import com.example.janghj.repository.dto.UserOrderDto;
//import com.example.janghj.repository.dto.UserOrderSearchDto;
//import com.querydsl.core.types.dsl.BooleanExpression;
//import com.querydsl.jpa.impl.JPAQueryFactory;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Repository;
//
//import static com.example.janghj.domain.QOrder.order;
//import static com.example.janghj.domain.User.QUser.user;
//
//@Repository
//@RequiredArgsConstructor
//public class UserRepositoryImpl {
//
//    private final JPAQueryFactory queryFactory;
//
//    public UserOrderDto userOrderSearch(UserOrderSearchDto userOrderSearchDto) {
//        return queryFactory
//                .select(new QUserOrderDto(
//                        user.id,
//                        user.kakaoId,
//                        user.username,
//                        user.email,
//                        user.role,
//                        user.address,
//                        order.id,
//                        order.totalAmount,
//                        order.orderProduct,
//                        order.orderStatus
//                ))
//                .from(user)
//                .join(user.order, order)
//                .where(userIdEq(userOrderSearchDto.getUserId()),
//                        orderIdEq(userOrderSearchDto.getOrderId()))
//                .fetchOne();
//    }
//
//    private BooleanExpression userIdEq(Long userId) {
//        return userId != null ? user.id.eq(Long.valueOf(userId)) : null;
//    }
//
//    private BooleanExpression orderIdEq(Long orderId) {
//        return orderId != null ? order.id.eq(Long.valueOf(orderId)) : null;
//    }
//}