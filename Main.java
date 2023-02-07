package carsharing;

import java.util.List;
import java.util.Scanner;

public class Main {

    static CompanyDao companyDao;

    public static void main(String[] args) {
        // write your code here
        String dbName = "carsharing";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-databaseFileName")) {
                dbName = args[i + 1];
                break;
            }
        }
        String dbURL = "jdbc:h2:./src/carsharing/db/" + dbName;
        companyDao = new CompanyDaoImpl(dbURL);
        Scanner sc = new Scanner(System.in);
        boolean repeat = true;
        while (repeat) {

            System.out.println("1. Log in as a manager\n2. Log in as a customer\n3. Create a customer\n0. Exit");
            int firstAnswer = sc.nextByte();

            switch (firstAnswer) {
                case 0:
                    repeat = false;
                    companyDao.exit();
                    break;
                case 1:
                    selectWhatToDo();
                    break;
                case 2:
                    customerList();
                    break;
                case 3:
                    createCustomer();
                    break;
                default:
                    System.out.println("Wrong choice");
                    break;
            }
        }
    }

    private static void selectWhatToDo() {
        Scanner sc = new Scanner(System.in);
        boolean repeat = true;
        while (repeat) {
            System.out.println("1. Company list\n2. Create a company\n0. Back");
            int userChoice = sc.nextInt();
            switch (userChoice) {
                case 0:
                    repeat = false;
                    break;
                case 1:
                    companyList("-");
                    break;
                case 2:
                    createCompany();
                    break;
                default:
                    System.out.println("Wrong choice");
                    break;
            }
        }
    }

    private static void customerList() {
        List<Customer> customers = companyDao.getAllCustomers();
        if (customers.isEmpty()) {
            System.out.println("The customer list is empty!");
        } else {
            System.out.println("Customer list:");
            for (Customer customer : customers) {
                System.out.println(customer.toString());
            }
            CustomerWhatToDo(customers);
        }
    }

    private static void createCustomer() {
        System.out.println("Enter the customer name:");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        companyDao.createCustomer(new Customer(name));
        System.out.println("The customer was added!");
    }

    private static void companyList(String customer) {
        List<Company> companies = companyDao.getAllCompanies();
        if (companies.isEmpty()) {
            System.out.println("The company list is empty!");
        } else {
            System.out.println("Choose a company:");
            for (Company company : companies) {
                System.out.println(company.toString());
            }
            if (!"-".equals(customer)) {
                System.out.println("0. Back");
                Scanner sc = new Scanner(System.in);
                int idCompany = sc.nextInt();
                if (idCompany != 0) {
                    System.out.println("Choose a car:");
                    List<Car> cars = companyDao.getAllCars(idCompany);
                    if (cars.isEmpty()) {
                        System.out.println("The car list is empty!");
                    } else {
                        int i = 1;
                        for (Car car : cars) {
                            System.out.println(i + ". " + car.getName());
                            i++;
                        }
                    }
                    System.out.println("0. Back");
                    int carNumber = sc.nextInt();
                    if (carNumber != 0) {
                        String carName = cars.get(carNumber - 1).getName();
                        System.out.println("You rented '" + carName + "'");
                        int carId = companyDao.getCarId(carName);
                        if (carId != -1) {
                            System.out.println(carName + " " + carId);
                            companyDao.updateCustomer(new Customer(customer), carId);
                        }
                    }
                }
            } else {
                carWhatToDo();
            }
        }
    }

    private static void createCompany() {
        System.out.println("Enter the company name:");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        companyDao.createCompany(new Company(name));
        System.out.println("The company was created!");
    }

    private static String findCompany(int idCompany) {
        String companyId = companyDao.getCompany(idCompany);
        return companyId;
    }

    private static void carWhatToDo() {
        System.out.println("0. Back");
        Scanner sc = new Scanner(System.in);
        int idCompany = sc.nextInt();
        if (idCompany != 0) {


            String ddd = findCompany(idCompany);
            if ("".equals(ddd)) {
                System.out.println("Wrong choice");
                companyList("-");
            } else {
                System.out.println("'" + ddd + "'" + " company");
            }
            boolean repeat = true;
            while (repeat) {
                System.out.println("1. Car list\n2. Create a car\n0. Back");
                int userChoice = sc.nextInt();
                switch (userChoice) {
                    case 0:
                        repeat = false;
                        break;
                    case 1:
                        carList(idCompany, false);
                        break;
                    case 2:
                        createCar(idCompany);
                        break;
                    default:
                        System.out.println("Wrong choice");
                        break;
                }
            }
        }
    }

    private static void carList(int idCompany, boolean customer) {
        List<Car> cars = companyDao.getAllCars(idCompany);
        if (cars.isEmpty()) {
            System.out.println("The car list is empty!");
        } else {
            if (!customer) {
                System.out.println("Car list:");
            }
            int i = 1;
            for (Car car : cars) {
                System.out.println(i + ". " + car.getName());
                i++;
            }
        }
    }

    private static void createCar(int idCompany) {

        System.out.println("Enter the car name:");
        Scanner sc = new Scanner(System.in);
        String name = sc.nextLine();
        companyDao.createCar(new Car(name, idCompany));
        System.out.println("The car was added!");
    }

    private static void CustomerWhatToDo(List<Customer> customers) {
        System.out.println("0. Back");
        Scanner sc = new Scanner(System.in);
        int idCustomer = sc.nextInt();
        String chosenCustomer;
        if (idCustomer != 0) {
            chosenCustomer = customers.get(idCustomer - 1).getName();
            if ("".equals(chosenCustomer)) {
                System.out.println("Wrong choice");
                companyList("-");
            }
            boolean repeat = true;
            while (repeat) {
                System.out.println("1. Rent a car\n2. Return a rented car\n3. My rented car\n0. Back");
                int userChoice = sc.nextInt();
                switch (userChoice) {
                    case 0:
                        repeat = false;
                        break;
                    case 1:
                        rentCar(chosenCustomer);
                        break;
                    case 2:
                        returnCar(chosenCustomer);
                        break;
                    case 3:
                        myRentedCar(chosenCustomer);
                        break;
                    default:
                        System.out.println("Wrong choice");
                        break;
                }
            }
        }
    }

    private static void rentCar(String chosenCustomer) {
        int customerCar = companyDao.checkCustomerCar(chosenCustomer);
        if (customerCar != -1) {
            System.out.println("You've already rented a car!");
        } else {
            companyList(chosenCustomer);
        }
    }

    private static void returnCar(String chosenCustomer) {
        int CustomerCar = companyDao.checkCustomerCar(chosenCustomer);
        if (CustomerCar == -1) {
            System.out.println("You didn't rent a car!");
        } else {
            System.out.println("You've returned a rented car!");
            companyDao.updateCustomer(new Customer(chosenCustomer), -1);
        }
    }

    private static void myRentedCar(String chosenCustomer) {
        int carId = companyDao.checkCustomerCar(chosenCustomer);
        if (carId == -1) {
            System.out.println("You didn't rent a car!");
        } else {
            System.out.println("Your rented car:");
            List<Car> car = companyDao.getCar(carId);
            System.out.println(car.get(0).getName());
            System.out.println("Company:\n" + findCompany(car.get(0).getId()));
        }
    }
}
