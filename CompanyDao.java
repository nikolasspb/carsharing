package carsharing;

import java.util.List;

public interface CompanyDao {
    List<Company> getAllCompanies();

    List<Car> getAllCars(int idCompany);

    List<Car> getCar(int carId);

    void createCompany(Company company);

    void createCar(Car car);

    String getCompany(int idCompany);

    void createCustomer(Customer customer);

    void updateCustomer(Customer customer, int carId);

    List<Customer> getAllCustomers();

    int checkCustomerCar(String chosenCustomer);

    int getCarId(String carName);

    void exit();
}
