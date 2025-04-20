**Sends a POST request to create a new price alert for a product.
The API receives the product URL, the user's desired price, email, and alert frequency.
This request is made to the local server running on port 8080.**

curl --location 'http://localhost:8080/api/alerts' \
--header 'Content-Type: application/json' \
--data-raw '{
    "productUrl" : "http://test.com/formal-shirt",
    "desiredPrice" : 749.00,
    "email" : "jasminejose17@gmail.com",
    "frequency": "evening"
    
}'


**This JSON response confirms that the price alert has been successfully created for the specified product.
"message": A clear confirmation message, including product name and alert price.
"email": The email address where the user will receive the notification once the product's price drops to or below the desired value.
Example Use Case:
A user sets an alert for a product priced at ₹749. When the price meets this condition, they'll be notified at jasminejose17@gmail.com.
This enhances user experience by providing immediate feedback and building trust in the alert system.**

{
    "message": "Alert set successfully for product:formal-shirt. You will be notified on below mail once your product price is less than or equal to 749.0, Thank you.",
    "email": "jasminejose17@gmail.com"
}

**This screenshot from DBeaver displays the alert table within the productpricetracker-db database. It shows that a user-defined price alert has been successfully recorded. The key details in the row include:
desired_price: 749 – the target price set by the user.
email: jasminejose17@gmail.com – the recipient of the alert notification.
product_url: http://test.com/formal-shirt – the URL of the product being monitored.
check_frequency: "0 15 20 * * ?" – a cron expression indicating the alert check is scheduled daily at 8:15 PM.
created_on / updated_on: Timestamps indicating the alert’s creation and last modification time.
This confirms the successful operation of the alert creation API and the proper persistence of alert preferences into the database.**


<img width="959" alt="image" src="https://github.com/user-attachments/assets/ea99c009-28a2-4f02-a6c6-eda974267bfb" />

**This screenshot displays a successful price drop alert email sent by the Price Drop Alert System to the user (jasminejose17@gmail.com). The alert notifies the user that the product "formal-shirt", previously being monitored, has dropped to a new price of ₹500.00, which is below the user’s set alert price of ₹749.00.**

<img width="951" alt="image" src="https://github.com/user-attachments/assets/6f365382-e3aa-4510-a2f1-9ef7884c585f" />
