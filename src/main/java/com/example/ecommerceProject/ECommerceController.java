package com.example.ecommerceProject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ecommerce")
public class ECommerceController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private orderDetailRepository orderDetailRepository;
    private HashMap<Object, Object> productQuantityMap;

    //Post Api - add a user
    @PostMapping("/users")
    public List<User> addUsers(@RequestBody List<User> users) {
        return userRepository.saveAll(users);
    }
    @PostMapping("/products")
    public List<Product> addProducts(@RequestBody List<Product> products) {
        return productRepository.saveAll(products);
    }

    //post api - create an order for all products with name starting with A
    @PostMapping("/orders")
    public Order createOrderForProductsStartingWithA(@RequestBody Order order) {
        return orderRepository.save(order);
    }
    //Get api - get the minimum price product for a given category
    @GetMapping("/products/max-price/{category}")
    public Product getMaxPricedProductByCategory(@PathVariable String category) {
        return productRepository.findTopByCategoryOrderByPriceDesc(category);
    }

    // GET API - Find the most ordered product (in case of multiples find the one with maximum price)
    @GetMapping("/products/most-ordered")
    public Product getMostOrderedProduct() {
        List<OrderDetail> allOrderDetails = orderDetailRepository.findAll();

        // Create a map to store the total quantity of each product ordered
        Map<Product, Integer> productQuantityMap = new HashMap<>();

        // Calculate the total quantity of each product ordered
        for (OrderDetail orderDetail : allOrderDetails) {
            Product product = orderDetail.getProduct();
            int quantity = orderDetail.getQuantity();
            productQuantityMap.put(product, productQuantityMap.getOrDefault(product, 0) + quantity);
        }

        // Find the product with the maximum total quantity
        Product mostOrderedProduct = null;
        int maxQuantity = 0;

        for (Map.Entry<Product, Integer> entry : productQuantityMap.entrySet()) {
            Product product = entry.getKey();
            int quantity = entry.getValue();

            if (quantity > maxQuantity || (quantity == maxQuantity && product.getPrice() > mostOrderedProduct.getPrice())) {
                mostOrderedProduct = product;
                maxQuantity = quantity;
            }
        }

        return mostOrderedProduct;
    }

}
