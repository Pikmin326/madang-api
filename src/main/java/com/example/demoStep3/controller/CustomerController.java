package com.example.demoStep3.controller;

import com.example.demoStep3.domain.Customer;
import org.springframework.web.bind.annotation.*;
import com.example.demoStep3.dto.CustomerDto;
import javax.sql.DataSource;
import java.security.AllPermission;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final DataSource dataSource;

    public CustomerController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping("/all")
    public List<Customer> getCustomersFromMySQL() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT custid, name, address, phone FROM Customer";

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()) {
                Customer c = new Customer(
                        rs.getInt("custid"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getString("phone")
                );
                customers.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    @GetMapping
    public List<CustomerDto> getCustomersWithViewDto() {
        List<CustomerDto> customers = new ArrayList<>();
        String sql = "SELECT custid, name, address FROM Customer";

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                customers.add(new CustomerDto(
                        rs.getInt("custid"),
                        rs.getString("name"),
                        rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return customers;
    }

    @GetMapping("/{id}")
    public CustomerDto getCustomerById(@PathVariable int id) {
        String sql = "SELECT custid, name, address FROM Customer WHERE custid = ?";

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new CustomerDto(
                            rs.getInt("custid"),
                            rs.getString("name"),
                            rs.getString("address")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @PutMapping("/{id}")
    public String updateCustomer(@PathVariable int id, @RequestBody CustomerDto dto) {
        String sql = "UPDATE Customer SET name = ? WHERE custid = ?";

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setString(1, dto.getName());
            stmt.setInt(2, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                return "수정 완료";
            } else {
                return "고객을 찾을 수 없습니다";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "에러 발생: " + e.getMessage();
        }
    }

    @PostMapping
    public String addCustomer(@RequestBody CustomerDto dto) {
        String sql = "INSERT INTO Customer (custid, name, address, phone) VALUES (?, ?, ?, ?)";

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, dto.getCustid());
            stmt.setString(2, dto.getName());
            stmt.setString(3, dto.getAddress() != null ? dto.getAddress() : "주소 미입력");
            stmt.setString(4, null);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                return "데이터베이스에 고객 추가 완료!";
            } else {
                return "고객 추가 실패";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "데이터베이스 오류 발생: " + e.getMessage();
        }
    }

    @DeleteMapping("/{id}")
    public String deleteCustomer(@PathVariable int id) {
        String sql = "DELETE FROM Customer WHERE custid = ?";

        try (
                Connection conn = dataSource.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
        ) {
            stmt.setInt(1, id);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                return "삭제 완료";
            } else {
                return "고객을 찾을 수 없습니다.";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "데이터베이스 오류 발생: " + e.getMessage();
        }
    }
}


/*
@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final List<Customer> customers = List.of(
            new Customer(1, "박지성", "영국 맨체스터", "000-5000-0001"),
            new Customer(2, "김연아", "대한민국 서울", "000-6000-0001"),
            new Customer(3, "김연경", "대한민국 경기도", "000-7000-0001"),
            new Customer(4, "추신수", "미국 클리블랜드", "000-8000-0001"),
            new Customer(5, "박세리", "대한민국 대전", null)
    );

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customers;
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(@PathVariable int id) {
        return customers.stream()
                .filter(c -> c.getCustid() == id)
                .findFirst()
                .orElse(null);
    }
}
*/