package co.edu.unbosque.microservice_order.client;

import co.edu.unbosque.microservice_order.model.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "msvc-investor")
public interface  InvestorClient {

    @GetMapping("/api/investor/{id}")
    UserDTO getUserById(@PathVariable Integer id);

    @PostMapping("/api/investor/account/{id}/balance/validate")
    void validateBalance(@PathVariable("id") Integer id, @RequestParam("amount") float amount);

    @PostMapping("/api/investor/account/{id}/balance/subtract")
    void subtractFromBalance(@PathVariable("id") Integer id, @RequestParam("amount") float amount);

    @PostMapping("/api/investor/account/{id}/balance/add")
    void addToBalance(@PathVariable("id") Integer id, @RequestParam("amount") float amount);

}
