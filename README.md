<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Football Reservation App</title>
</head>
<body>
    <h1>Football Reservation App</h1>

    <h2>Overview</h2>
    <p>The Football Reservation App is an Android application that allows users to reserve football fields for specific times and dates. It includes an admin dashboard for managing fields and viewing reservation statistics. The app is built using Java, XML for the UI, and SQLite for local database management.</p>

    <h2>Features</h2>
    <h3>For Admins</h3>
    <ul>
        <li><strong>Dashboard:</strong> View statistics on reservations, such as the number of reservations made over time.</li>
        <li><strong>Field Management:</strong> Add, update, and delete football fields.</li>
        <li><strong>User Management:</strong> View and manage user accounts.</li>
    </ul>

    <h3>For Users</h3>
    <ul>
        <li><strong>Field Reservation:</strong> Reserve a football field for a specific time and date.</li>
        <li><strong>User Registration:</strong> Register for a new account.</li>
        <li><strong>User Login:</strong> Log in to an existing account.</li>
    </ul>

    <h2>Technologies Used</h2>
    <ul>
        <li><strong>Android Development:</strong> Java and XML</li>
        <li><strong>Database:</strong> SQLite</li>
        <li><strong>Sessions:</strong> Handled using Android's built-in session management techniques</li>
    </ul>

    <h2>Installation</h2>
    <ol>
        <li><strong>Clone the Repository</strong>
            <pre><code>git clone https://github.com/yourusername/football-reservation-app.git</code></pre>
        </li>
        <li><strong>Open the Project</strong>
            <p>Import the project into Android Studio.</p>
        </li>
        <li><strong>Build and Run</strong>
            <p>Use Android Studio to build and run the app on an emulator or a physical device.</p>
        </li>
    </ol>

    <h2>Usage</h2>
    <h3>Admin Interface</h3>
    <ul>
        <li><strong>Login:</strong> Access the admin dashboard by logging in with admin credentials.</li>
        <li><strong>Dashboard:</strong> View reservation statistics and manage fields from the dashboard.</li>
        <li><strong>Field Management:</strong> Add, update, or delete fields through the admin interface.</li>
    </ul>

    <h3>User Interface</h3>
    <ul>
        <li><strong>Register:</strong> Create a new account from the registration screen.</li>
        <li><strong>Login:</strong> Log in to your account from the login screen.</li>
        <li><strong>Reserve Field:</strong> Navigate to the reservation screen to book a field for a specific time and date.</li>
    </ul>

    <h2>Database</h2>
    <p>The app uses SQLite for local data storage. The database schema includes tables for users, fields, and reservations. Ensure that the SQLite database is properly initialized and managed within the app.</p>

    <h2>Contributing</h2>
    <p>Contributions are welcome! To contribute to the project:</p>
    <ul>
        <li>Fork the repository on GitHub.</li>
        <li>Create a new branch for your changes.</li>
        <li>Implement your changes and test them thoroughly.</li>
        <li>Submit a pull request with a detailed description of your changes.</li>
    </ul>

    <h2>Contact</h2>
    <p>For any questions or support, please contact <a href="mailto:mouad.elbouchraoui@gmail.com">mouad.elbouchraoui@gmail.com</a>.</p>
</body>
</html>
