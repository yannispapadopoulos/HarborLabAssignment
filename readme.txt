
//////////////////////////////////////////////ENDPOINTS/////////////////////////////////////////////////

I have added swagger documentation, which you can see here:  http://localhost:8081/swagger-ui/index.html 

1)GET /api/bookings
eg: 	GET /api/bookings?roomId=1&date=2024-12-12
	Content-Type: application/json

*this REST API gets all the bookings that are active(not cancelled). I have added a Boolean active status field in the booking table for that reason.


2)POST /api/bookings
eg:	POST /api/bookings
	Content-Type: application/json

	{
    	"roomId": 1,
    	"employeeEmail": "new.employee@example.com",
    	"bookingDate": "2024-12-12",
    	"timeFrom": "12:00:00",
    	"timeTo": "13:00:00"
	}


3)PATCH /api/bookings/{id}
eg:	PATCH /api/bookings/1
	Content-Type: application/json

*I considered Cancel Booking as a soft delete.
*So, this REST API basically updates the record with the active status field as false.

*However, i included the following hard delete API too, just in case it is was needed as mandatory.

4)DELETE /api/bookings/{id}/delete
eg:	DELETE /api/bookings/1
	Content-Type: application/json


5)GET /api/rooms
eg:	GET /api/rooms
	Content-Type: application/json


///////////////////////////////////////////////DATABASE/////////////////////////////////////////////////////

I used h2 database.
After you run the application, you can reach the database UI here: http://localhost:8081/h2-console/
Below are some test data for your convenience:

INSERT INTO Room (id, name, capacity) VALUES 
(1, 'Conference A', 10),
(2, 'Meeting Room B', 6),
(3, 'Huddle C', 4),
(4, 'Training Room', 20);

INSERT INTO Booking (id, room_id, employee_email, booking_date, time_from, time_to, active) VALUES
(1, 1, 'john.doe@example.com', '2024-12-12', '10:00:00', '11:00:00', true),
(2, 1, 'jane.smith@example.com', '2024-12-12', '11:00:00', '12:00:00', true),
(3, 2, 'alice.wonder@example.com', '2024-12-12', '09:00:00', '11:00:00', true),
(4, 2, 'bob.builder@example.com', '2024-12-11', '14:00:00', '15:00:00', true),
(5, 3, 'charlie.brown@example.com', '2024-12-13', '13:00:00', '15:00:00', false), -- Canceled booking
(6, 4, 'diana.prince@example.com', '2024-12-14', '10:00:00', '12:00:00', true),
(7, 1, 'clark.kent@example.com', '2024-12-10', '09:00:00', '10:00:00', true);
