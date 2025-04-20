# Sends a POST request to create a new price alert for a product.
# The API receives the product URL, the user's desired price, email, and alert frequency.
# This request is made to the local server running on port 8080.

curl --location 'http://localhost:8080/api/alerts' \
--header 'Content-Type: application/json' \
--data-raw '{
    "productUrl" : "http://test.com/formal-shirt",
    "desiredPrice" : 749.00,
    "email" : "jasminejose17@gmail.com",
    "frequency": "evening"
    
}'


# This JSON response confirms that the price alert has been successfully created for the specified product.
# - "message": A clear confirmation message, including product name and alert price.
# - "email": The email address where the user will receive the notification once the product's price drops to or below the desired value.

  # Example Use Case:
  # A user sets an alert for a product priced at ₹749. When the price meets this condition, they'll be notified at jasminejose17@gmail.com.

  # This enhances user experience by providing immediate feedback and building trust in the alert system.

{
    "message": "Alert set successfully for product:formal-shirt. You will be notified on below mail once your product price is less than or equal to 749.0, Thank you.",
    "email": "jasminejose17@gmail.com"
}

![image](https://github.com/user-attachments/assets/2353b1a1-a41b-480b-a43e-ba10b1e2b306)


<img width="951" alt="image" src="https://github.com/user-attachments/assets/6f365382-e3aa-4510-a2f1-9ef7884c585f" />
