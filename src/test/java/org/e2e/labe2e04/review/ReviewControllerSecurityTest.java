package org.e2e.labe2e04.review;

import org.e2e.labe2e04.coordinate.domain.Coordinate;
import org.e2e.labe2e04.driver.domain.Category;
import org.e2e.labe2e04.driver.domain.Driver;
import org.e2e.labe2e04.driver.infrastructure.DriverRepository;
import org.e2e.labe2e04.exception.ResourceNotFoundException;
import org.e2e.labe2e04.passenger.domain.Passenger;
import org.e2e.labe2e04.passenger.infrastructure.PassengerRepository;
import org.e2e.labe2e04.review.domain.Review;
import org.e2e.labe2e04.review.infrastructure.ReviewRepository;
import org.e2e.labe2e04.ride.domain.Ride;
import org.e2e.labe2e04.ride.domain.Status;
import org.e2e.labe2e04.ride.infrastructure.RideRepository;
import org.e2e.labe2e04.user.domain.Role;
import org.e2e.labe2e04.utils.Reader;
import org.e2e.labe2e04.vehicle.domain.Vehicle;
import org.e2e.labe2e04.vehicle.infrastructure.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WithUserDetails(value = "janedoe@example.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerSecurityTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private Reader reader;

    @Autowired
    private VehicleRepository vehicleRepository;

    @BeforeEach
    public void setUp() throws Exception {
        reviewRepository.deleteAll();
        rideRepository.deleteAll();
        driverRepository.deleteAll();
        passengerRepository.deleteAll();
        vehicleRepository.deleteAll();

        mockMvc.perform(post("/auth/register")
                .contentType(APPLICATION_JSON)
                .content(Reader.readJsonFile("/passenger/post.json")));

        mockMvc.perform(post("/auth/register")
                .contentType(APPLICATION_JSON)
                .content(Reader.readJsonFile("/driver/post.json")));

        Coordinate origin = new Coordinate();
        origin.setLatitude(37.775938);
        origin.setLongitude(-122.419664);

        Coordinate destination = new Coordinate();
        destination.setLatitude(37.775938);
        destination.setLongitude(-122.419664);

        Passenger passenger = passengerRepository
                .findByEmail("janedoe@example.com")
                .orElseThrow(() -> new IllegalStateException("Passenger not found"));

        //noinspection ExtractMethodRecommender
        Vehicle vehicle = new Vehicle();
        vehicle.setCapacity(4);
        vehicle.setFabricationYear(2000);
        vehicle.setBrand("Toyota");
        vehicle.setLicensePlate("ABC123");
        vehicle.setModel("Toyota");
        vehicle.setColor("Red");

        Driver driver = new Driver();
        driver.setRole(Role.DRIVER);
        driver.setEmail("example@mail.com");
        driver.setFirstName("John");
        driver.setLastName("Doe");
        driver.setPassword("password");
        driver.setPhoneNumber("1234567890");
        driver.setCategory(Category.X);
        driver.setVehicle(vehicle);
        driverRepository.save(driver);

        Ride ride = new Ride();
        ride.setOriginName("Home");
        ride.setDestinationName("School");
        ride.setPrice(100d);
        ride.setOriginCoordinates(origin);
        ride.setDestinationCoordinates(destination);
        ride.setStatus(Status.valueOf("REQUESTED"));
        ride.setPassenger(passenger);
        ride.setDriver(driver);
        rideRepository.save(ride);
    }

    @Test
    public void testAuthorizedAccessToCreateReview() throws Exception {
        Long rideId = rideRepository.findAll().get(0).getId();
        Long passengerId = passengerRepository.findAll().get(0).getId();
        String jsonContent = Reader.readJsonFile("/review/post.json");
        jsonContent = reader.updateReviewRelatioshipsId(jsonContent, "rideId", rideId);
        jsonContent = reader.updateReviewRelatioshipsId(jsonContent, "targetId", passengerId);
        mockMvc.perform(post("/review")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isCreated());
    }

    @Test
    @WithAnonymousUser
    public void testUnauthorizedAccessToCreateReview() throws Exception {
        Long rideId = rideRepository.findAll().get(0).getId();
        Long targetId = driverRepository.findAll().get(0).getId();
        String jsonContent = Reader.readJsonFile("/review/post.json");
        jsonContent = reader.updateReviewRelatioshipsId(jsonContent, "rideId", rideId);
        jsonContent = reader.updateReviewRelatioshipsId(jsonContent, "targetId", targetId);
        mockMvc.perform(post("/review")
                        .contentType(APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testAuthorizedAccessToDeleteReview() throws Exception {
        Long reviewId = createReview();
        mockMvc.perform(delete("/review/{id}", reviewId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "DRIVER", value = "other@example.com")
    public void testUnauthorizedAccessToDeleteReview() throws Exception {
        Long reviewId = createReview();
        mockMvc.perform(delete("/review/{id}", reviewId)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private Long createReview() throws Exception {
        Review review = new Review();
        Passenger author = passengerRepository
                .findByEmail("janedoe@example.com")
                .orElseThrow(() -> new ResourceNotFoundException("Passenger not found"));
        review.setAuthor(author);
        Driver target = driverRepository
                .findByEmail("johndoe@example.com")
                .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        review.setTarget(target);
        Ride ride = rideRepository
                .findAll()
                .get(0);
        review.setRide(ride);
        review.setComment("Good ride");
        review.setRating(5);
        return reviewRepository.save(review).getId();
    }
}
