package com.buildingweb.config;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.buildingweb.entity.Building;
import com.buildingweb.entity.Customer;
import com.buildingweb.entity.RentArea;
import com.buildingweb.entity.Role;
import com.buildingweb.entity.Transaction;
import com.buildingweb.entity.User;
import com.buildingweb.enums.District;
import com.buildingweb.enums.RoleConst;
import com.buildingweb.enums.StatusConst;
import com.buildingweb.enums.TransactionConst;
import com.buildingweb.repository.BuildingRepository;
import com.buildingweb.repository.CustomerRepository;
import com.buildingweb.repository.RentAreaRepository;
import com.buildingweb.repository.RoleRepository;
import com.buildingweb.repository.TransactionRepository;
import com.buildingweb.repository.UserRepository;
import com.github.javafaker.Faker;

@Component
public class DataLoaderConfig implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private RentAreaRepository rentAreaRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private static final Random RANDOM = new Random();

    public static <T extends Enum<?>> T randomEnum(Class<T> clazz) {
        int x = RANDOM.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    private static final String[] RENT_TYPES = { "TANG_TRET", "NGUYEN_CAN", "NOI_THAT" };

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        Role role1 = new Role();
        role1.setCode(RoleConst.STAFF);
        role1.setName("Nhân viên");
        Role role2 = new Role();
        role2.setCode(RoleConst.MANAGER);
        role2.setName("Quản lý");
        roleRepository.save(role1);
        roleRepository.save(role2);

        // user fake
        for (int i = 0; i < 50; i++) {
            User user = new User();
            Building buildingUser = null;
            user.setUsername(faker.name().username());
            user.setFullname(faker.name().fullName());
            user.setEmail(faker.internet().emailAddress());
            user.setPhoneNumber(faker.phoneNumber().phoneNumber());
            user.setAvatar(faker.avatar().image().getBytes());
            user.setStatus(1);
            user.setPassword(passwordEncoder.encode("321323"));

            if (faker.bool().bool()) {
                user.getRoles().add(role1);
            } else if (faker.bool().bool()) {
                user.getRoles().add(role2);

            } else if (faker.bool().bool()) {
                user.getRoles().add(role1);
                user.getRoles().add(role2);
            }
            userRepository.save(user);
            for (int k = 0; k < 5; k++) {
                Building building = new Building();
                building.setName(faker.name().bloodGroup());
                building.setStreet(faker.address().streetAddress());
                building.setWard(faker.address().state());
                building.setDistrict(randomEnum(District.class));
                building.setStructure(faker.address().longitude());
                building.setNumberOfBasement(faker.number().randomNumber());
                building.setFloorArea(faker.number().randomNumber());
                building.setDirection(faker.ancient().hero());
                building.setLevel(faker.business().creditCardType());
                building.setRentPrice(faker.number().randomNumber());
                building.setRentPriceDescription(faker.app().name());
                building.setServiceFee(faker.number().randomNumber());
                building.setCarFee(faker.number().randomNumber());
                building.setMotorFee(faker.number().randomNumber());
                building.setOvertimeFee(faker.number().randomNumber());
                building.setWaterFee(faker.number().randomNumber());
                building.setElectricity(faker.number().randomNumber());
                building.setDeposit(faker.number().randomNumber());
                building.setPayment(faker.number().randomNumber());
                building.setRentTime(faker.number().randomNumber());
                building.setDecorationTime(faker.number().randomNumber());
                building.setBrokerageFee(new BigDecimal(faker.number().randomDouble(2, 0, 1000)));
                building.setNote(faker.lorem().paragraph(30));
                building.setManagerName(faker.name().fullName());
                building.setManagerPhoneNumber(faker.phoneNumber().phoneNumber());
                building.setLinkOfBuilding(faker.lorem().paragraph(30).getBytes(StandardCharsets.UTF_8));
                building.setRentTypes(getRandomRentTypes(RANDOM));
                buildingRepository.save(building);
                if (faker.bool().bool() && user.isStaff()) {
                    building.getUsers().add(user);
                }
                for (int o = 0; o < faker.random().nextInt(1, 10); o++) {
                    RentArea rentArea = RentArea.builder().building(building).value(faker.random().nextLong(1400) + 100)
                            .build();
                    rentAreaRepository.save(rentArea);
                    building.getRentAreas().add(rentArea);
                }
                buildingRepository.save(building);
            }
            // customer fake
            for (int j = 0; j < 10; j++) {
                Customer customer = new Customer();
                customer.setCompanyName(faker.company().name());
                customer.setFullname(faker.name().fullName());
                customer.setEmail(faker.internet().emailAddress());
                customer.setPhone(faker.phoneNumber().phoneNumber());
                customer.setDemand(faker.bothify("123"));
                customer.setIsActive(1);
                customer.setStatus(randomEnum(StatusConst.class));
                Boolean isTransaction = false;
                if (user.isStaff() && faker.bool().bool()) {
                    customer.getUsers().add(user);
                    isTransaction = true;
                }
                customerRepository.save(customer);
                for (int r = 0; r < 10 && isTransaction; r++) {
                    Customer getCus = customerRepository.findByIdAndIsActive(customer.getId(), 1);
                    User user1 = userRepository.findByIdAndStatus(user.getId(), 1);
                    if (user1.isStaff() && faker.bool().bool()) {
                        Transaction transaction = Transaction.builder().customer(getCus).user(user1)
                                .note(faker.lorem().paragraph(6))
                                .code(randomEnum(TransactionConst.class)).build();
                        transactionRepository.save(transaction);
                    }
                }
            }
        }
    }

    private static String getRandomRentTypes(Random random) {
        StringBuilder rentTypesBuilder = new StringBuilder();

        // Random số lượng phần tử cần lấy từ 1 đến 3
        int numberOfTypes = random.nextInt(3) + 1;

        for (int i = 0; i < numberOfTypes; i++) {
            // Lấy ngẫu nhiên một phần tử từ mảng
            String randomType = RENT_TYPES[random.nextInt(RENT_TYPES.length)];
            if (rentTypesBuilder.length() > 0) {
                rentTypesBuilder.append(",");
            }
            rentTypesBuilder.append(randomType);
        }

        return rentTypesBuilder.toString();
    }

}