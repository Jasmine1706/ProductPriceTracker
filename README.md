curl --location 'http://localhost:8080/api/alerts' \
--header 'Content-Type: application/json' \
--data-raw '{
    "productUrl" : "http://test.com/formal-shirt",
    "desiredPrice" : 749.00,
    "email" : "jasminejose17@gmail.com",
    "frequency": "now"
    
}'

{
    "message": "Alert set successfully for product:formal-shirt. You will be notified on below mail once your product price is less than or equal to 749.0, Thank you.",
    "email": "jasminejose17@gmail.com"
}

<img width="958" alt="image" src="https://github.com/user-attachments/assets/c31a8c08-528d-4b42-87fa-45af0cdb1e57" />


<img width="692" alt="image" src="https://github.com/user-attachments/assets/a1e1d296-0512-413a-b98b-96d34b331ad3" />
