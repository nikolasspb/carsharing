package carsharing;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoImpl implements CompanyDao {
    private Connection conn;
    private static final String JDBC_DRIVER = "org.h2.Driver";

    public CompanyDaoImpl(String dbURL) {
        Statement stmt = null;
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(dbURL);
            conn.setAutoCommit(true);
            stmt = conn.createStatement();
            String sql;
            sql = "CREATE TABLE IF NOT EXISTS COMPANY " +
                    "(id INT not NULL AUTO_INCREMENT, " +
                    " NAME VARCHAR(255) UNIQUE NOT NULL, " +
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS CAR " +
                    "(id INT AUTO_INCREMENT PRIMARY KEY, " +
                    " NAME VARCHAR(255) UNIQUE NOT NULL, " +
                    " COMPANY_ID INT not NULL, " +
                    " CONSTRAINT fk_COMPANY FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(id)) ";
            stmt.executeUpdate(sql);
            sql = "CREATE TABLE IF NOT EXISTS CUSTOMER " +
                    "(id INT AUTO_INCREMENT PRIMARY KEY, " +
                    " NAME VARCHAR(255) UNIQUE NOT NULL, " +
                    " RENTED_CAR_ID INT, " +
                    " CONSTRAINT fk_CAR FOREIGN KEY (RENTED_CAR_ID) REFERENCES CAR(id)) ";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
        }
    }

    @Override
    public List<Company> getAllCompanies() {
        List<Company> companies = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "SELECT id, NAME FROM COMPANY";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                companies.add(new Company(rs.getInt("id"), rs.getString("NAME")));
            }
            rs.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
        }
        return companies;
    }

    @Override
    public void createCompany(Company company) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "INSERT INTO COMPANY " + "(`NAME`) VALUES ('" + company.getName() + "')";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
        }
    }

    @Override
    public String getCompany(int idCompany) {
        Statement stmt = null;
        String name = "";
        try {
            stmt = conn.createStatement();
            String sql = "SELECT NAME FROM COMPANY WHERE id = " + idCompany;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                name = rs.getString("NAME");
            }
            rs.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
        }

        return name;
    }

    @Override
    public List<Car> getAllCars(int idCompany) {
        List<Car> cars = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "SELECT RENTED_CAR_ID FROM CUSTOMER WHERE RENTED_CAR_ID IS NOT NULL ";
            ResultSet rs = stmt.executeQuery(sql);
            StringBuilder sb = new StringBuilder();

            while (rs.next()) {
                sb.append(rs.getInt("RENTED_CAR_ID") + ",");
            }
            if (sb.length() == 0) {
                sql = "SELECT id, NAME FROM CAR WHERE COMPANY_ID = " + idCompany + "";
            } else {
                sql = "SELECT id, NAME FROM CAR WHERE COMPANY_ID = " + idCompany + " AND ID != ALL(" + sb + ")";
            }

            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                cars.add(new Car(rs.getString("NAME"), rs.getInt("id")));
            }
            rs.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
        }
        return cars;
    }

    @Override
    public List<Car> getCar(int carId) {
        List<Car> cars = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "SELECT NAME, COMPANY_ID FROM CAR WHERE ID = " + carId;
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                cars.add(new Car(rs.getString("NAME"), rs.getInt("COMPANY_ID")));
            }
            rs.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
        }
        return cars;
    }

    @Override
    public void createCar(Car car) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "INSERT INTO CAR (NAME, COMPANY_ID) VALUES ('" + car.getName() + "'," + car.getId() + ")";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
        }
    }

    @Override
    public void createCustomer(Customer customer) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "INSERT INTO CUSTOMER (NAME) VALUES ('" + customer.getName() + "')";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
        }
    }

    @Override
    public void updateCustomer(Customer customer, int carId) {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sqlString = "NULL";
            if (carId != -1) {
                sqlString = "" + carId;
            }
            String sql = "UPDATE CUSTOMER SET RENTED_CAR_ID = " + sqlString + " WHERE NAME = '" + customer.getName() + "'";
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            String sql = "SELECT id, NAME FROM CUSTOMER";
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                customers.add(new Customer(rs.getString("NAME"), rs.getInt("id")));
            }
            rs.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
        }
        return customers;
    }

    @Override
    public int checkCustomerCar(String chosenCustomer) {
        Statement stmt = null;
        int result = -1;
        try {
            stmt = conn.createStatement();
            String sql = "SELECT RENTED_CAR_ID FROM CUSTOMER WHERE RENTED_CAR_ID IS NOT NULL and NAME = '" + chosenCustomer + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                result = rs.getInt("RENTED_CAR_ID");
            }
            rs.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
        }
        return result;
    }

    @Override
    public int getCarId(String carName) {
        Statement stmt = null;
        int carId = -1;
        try {
            stmt = conn.createStatement();
            String sql = "SELECT id FROM CAR WHERE NAME = '" + carName + "'";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                carId = rs.getInt("id");
            }
            rs.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
        }
        return carId;
    }

    @Override
    public void exit() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
