package com.rahul.electronic.store.serviceImpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.rahul.electronic.store.dto.CreateOrderRequest;
import com.rahul.electronic.store.dto.OrderDto;
import com.rahul.electronic.store.dto.PageableResponse;
import com.rahul.electronic.store.dto.ProductDto;
import com.rahul.electronic.store.entity.Cart;
import com.rahul.electronic.store.entity.CartItem;
import com.rahul.electronic.store.entity.Order;
import com.rahul.electronic.store.entity.OrderItems;
import com.rahul.electronic.store.entity.Product;
import com.rahul.electronic.store.entity.User;
import com.rahul.electronic.store.exception.BadApiRequestException;
import com.rahul.electronic.store.exception.ResourceNotFoundException;
import com.rahul.electronic.store.helper.Helper;
import com.rahul.electronic.store.repo.CartRepo;
import com.rahul.electronic.store.repo.OrderRepo;
import com.rahul.electronic.store.repo.UserRepo;
import com.rahul.electronic.store.service.OrderService;
@Service
public class OrderServiceImpl implements OrderService {
	Logger log=LoggerFactory.getLogger(OrderServiceImpl.class);
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private OrderRepo orderRepo;
	@Autowired
	ModelMapper mapper;

	@Autowired
	CartRepo cartRepo;

	@Override
	public OrderDto createOrder(CreateOrderRequest orderDto) {
		
		String userId=orderDto.getUserId();
		String cartId=orderDto.getCartId();
		
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("No User Found For Given Id !!"));

		// fetch cart
		Cart cart = cartRepo.findById(cartId)
				.orElseThrow(() -> new ResourceNotFoundException("Cart is not found For Given Id !!"));
		List<CartItem> cartItems = cart.getItems();
		if (cartItems.size() <= 0) {
			throw new BadApiRequestException("Invalid number of items in cart !!");
		}

		Order order = Order.builder().billingName(orderDto.getBillingName()).billingPhone(orderDto.getBillingPhone())
				.billingAddress(orderDto.getBillingAddress()).orderDate(new Date())
				.deliveredDate(null).paymentStatus(orderDto.getPaymentStatus())
				.orderStatus(orderDto.getOrderStatus()).orderId(UUID.randomUUID().toString()).user(user).build();

		AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
		List<OrderItems> orderItems = cartItems.stream().map(cartItem -> {
			OrderItems orderItem = OrderItems.builder().quantity(cartItem.getQuantity()).product(cartItem.getProduct())
					.totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice()).order(order)
					.build();

			orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());

			return orderItem;
		}).collect(Collectors.toList());
		log.info("Order Item List is {}" ,orderItems );
		order.setOrderItems(orderItems);
		order.setOrderAmount(orderAmount.get());
		
		log.info("Order  List is {}" ,order );
		Order savedorder=orderRepo.save(order);
		log.info("Order Item List is {}" ,savedorder);
		cart.getItems().clear();
		cartRepo.save(cart);
		return mapper.map(savedorder, OrderDto.class);
	}

	@Override
	public void removeOrder(String orderId) {
		Order order=orderRepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order is not found For Given Id !!"));
		orderRepo.delete(order);
	}

	@Override
	public List<OrderDto> getOrderOfuser(String userId) {
		User user = userRepo.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("No User Found For Given Id !!"));
		
		List<Order> orders=orderRepo.findByUser(user);
		List<OrderDto> ordersDto=orders.stream().map(order -> mapper.map(orders, OrderDto.class)).collect(Collectors.toList());
		return ordersDto;

	}

	@Override
	public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
		
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		Pageable pageable=PageRequest.of(pageNumber, pageSize, sort);
		
		Page<Order> page =orderRepo.findAll(pageable);
		PageableResponse<OrderDto> response = Helper.getpageableResponse(page, OrderDto.class);
		return response;
	}

}
