//const val PI = 3.14  // const defined outside main, value must be known before execution
import kotlin.math.*

fun main() {

    // ── VARIABLES ────────────────────────────────────
    // var: mutable — can be reassigned
    // val: immutable — cannot be reassigned
    // const: like val but defined outside main, evaluated at compile time
    //
    // in real code, default to val — only use var when you know the value will change
    // e.g. a user's score changes, their id never does

    var name: String = "Emmanuel"
    var age: Int = 19
    println("Name: $name, Age: $age")

    name = "Daniel"  // var — allowed
    // val id = 1
    // id = 2  // ❌ won't compile — val cannot be reassigned


    // ── DATA TYPES ───────────────────────────────────
    // Kotlin is statically typed — every variable has a type the compiler knows at compile time
    // you rarely write the type explicitly because of type inference (see below)

    var n: Int = 10          // whole numbers — user age, item count, page number
    var l: Long = 100L       // bigger whole numbers — timestamps, file sizes
    var d: Double = 3.14     // decimal numbers — prices, coordinates, tax rates
    var f: Float = 3.14F     // less precise decimal — used in graphics/animations
    var b: Boolean = true    // true or false — flags, toggles, permissions
    var s: String = "Kakashi" // text
    var c: Char = 'A'        // single character
    var a: Any = "10"        // can hold any type — avoid, loses type safety

    // type inference — Kotlin figures out the type from the value you assign
    // you'll write this form 99% of the time in real code
    var city = "Lagos"    // inferred as String
    var score = 100       // inferred as Int
    var price = 29.99     // inferred as Double
    var isLoggedIn = true // inferred as Boolean


    // ── NULL SAFETY ──────────────────────────────────
    // null means "no value" — the data doesn't exist, wasn't provided, or wasn't found
    //
    // in Java, any variable can silently be null and the compiler won't warn you
    // you only find out at runtime when the app crashes with NullPointerException
    //
    // Kotlin fixes this by splitting every type into two versions:
    //   String  → guaranteed to have a value, compiler won't let you assign null
    //   String? → explicitly allowed to be null, compiler forces you to handle it
    //
    // you rarely write String? = null yourself in real code
    // the ? comes from situations where data might genuinely not exist:
    //
    //   val user = userRepository.findById(id)        // User?  — user might not exist in DB
    //   val token = request.headers["Authorization"]  // String? — header might not be sent
    //   val input = readLine()                        // String? — user might enter nothing
    //   val age = "abc".toIntOrNull()                 // Int?   — conversion might fail
    //
    // the ? in the type comes from the function's return type, not from you
    // you just handle whatever comes back safely with ?. ?: or ?.let

    // simulating what you get back from a database lookup
    fun findUserEmail(id: Int): String? {
        return if (id == 1) "dan@mail.com" else null  // only user 1 exists
    }

    val emailFromDb = findUserEmail(1)   // String? — might be null
    val missingEmail = findUserEmail(99) // String? — this one is null


    // ?. SAFE CALL
    // instead of: if (email != null) email.uppercase()
    // ?. does the null check inline — if null, skips the call and returns null

    println(emailFromDb?.uppercase())   // DAN@MAIL.COM
    println(missingEmail?.uppercase())  // null — no crash


    // ?: ELVIS OPERATOR
    // you don't want "null" showing up in your UI or API response
    // ?: gives you a fallback when the left side is null
    // read it as: "use this, OR IF null, use that instead"

    val displayEmail = emailFromDb ?: "No email on file"
    val displayMissing = missingEmail ?: "No email on file"
    println(displayEmail)    // dan@mail.com
    println(displayMissing)  // No email on file

    // real backend use — return a default when DB field is empty
    fun getDisplayName(id: Int): String {
        val name2 = findUserEmail(id)
        return name2 ?: "Guest"
    }


    // ?.let — RUN A BLOCK ONLY IF NOT NULL
    // ?: gives you a fallback value
    // ?.let gives you a fallback block — do multiple things only if the value exists
    // 'it' inside the block is the confirmed non-null value

    emailFromDb?.let {
        // entire block only runs because emailFromDb is not null
        // 'it' here is "dan@mail.com" — guaranteed non-null inside
        println(it.uppercase())    // DAN@MAIL.COM
        println(it.length)         // 13
        println(it.split("@")[0])  // dan
    }

    missingEmail?.let {
        println("this never runs — block skipped because missingEmail is null")
    }

    // real Android use — only update the UI if data came back
    // user?.let {
    //     nameTextView.text = it.name
    //     emailTextView.text = it.email
    //     profileImage.load(it.avatarUrl)
    // }


    // NULLABLE BOOLEAN
    // Boolean  → 2 states: true / false
    // Boolean? → 3 states: true / false / null
    //
    // comes up when a flag in your DB hasn't been set yet
    // e.g. isVerified might be null for users who just registered
    // it's not false — it's unknown, the email hasn't been confirmed yet

    val isVerified: Boolean? = null

    // can't use if (isVerified) — compiler rejects it, Boolean? is not Boolean
    // == true only passes for actual true, null goes to else
    if (isVerified == true) {
        println("Verified — show full access")
    } else {
        println("Not verified or unknown — show restricted access")  // prints this
    }

    // alternative — collapse null into false with elvis, then check
    if (isVerified ?: false) {
        println("Verified")
    } else {
        println("Not verified or unknown")  // prints this
    }


    // ── STRINGS ──────────────────────────────────────
    // String is one of the most used types in Android and backend
    // you'll constantly be validating, formatting, and transforming text

    // .trim() — strip surrounding whitespace
    // always trim before validating user input — users accidentally add spaces
    val rawEmail = "   user@gmail.com   "
    println(rawEmail.trim())  // user@gmail.com

    // .length — character count, use for input validation
    val inputUsername = "dan"
    if (inputUsername.length < 5) println("username too short")

    // .uppercase() / .lowercase() — returns a NEW string, original unchanged
    val userRole = "ADMIN"
    println(userRole.lowercase())  // admin
    println(userRole.uppercase())  // ADMIN

    // case-insensitive comparison — normalize both sides before comparing
    // so "ADMIN" == "admin" == "Admin" all match
    if ("admin".equals(userRole, ignoreCase = true)) println("is admin")

    // .contains() — check if text exists inside another string
    // case-sensitive by default
    val bio = "Android developer from Abuja"
    println(bio.contains("Android"))                     // true
    println(bio.contains("android"))                     // false
    println(bio.contains("android", ignoreCase = true))  // true

    // .replace() — swap text, returns a new string
    // real use: sanitize phone numbers before saving to DB
    val phone = "+234-801-000-0000"
    println(phone.replace("-", ""))  // +2348010000000

    // .split() — break a string into a List<String> at every delimiter
    // real use: parse CSV data or comma-separated query params from an API
    val csv = "name,age,city"
    val fields = csv.split(",")
    println(fields[0])  // name

    // .substring(start, end) — extract a portion, start inclusive, end exclusive
    // real use: parse structured strings like dates from an API response
    val date = "2025-06-13"
    println(date.substring(0, 4))  // 2025 — year
    println(date.substring(5, 7))  // 06   — month

    // .startsWith() / .endsWith() — check beginning or end of a string
    // real use: validate URLs, check file types before uploading
    val apiUrl = "https://api.example.com/users"
    println(apiUrl.startsWith("https"))  // true — only accept secure URLs
    println(apiUrl.endsWith("/users"))   // true

    val fileName = "profile_picture.jpg"
    println(fileName.endsWith(".jpg"))   // true
    println(fileName.endsWith(".png"))   // false

    // .isEmpty() vs .isBlank()
    // isEmpty() → true only if the string has zero characters
    // isBlank()  → true if empty OR contains only whitespace
    // always use isBlank() for validating user input — a string of spaces is useless
    val emptyField = ""
    val whitespaceField = "     "
    println(emptyField.isEmpty())      // true
    println(whitespaceField.isEmpty()) // false — space characters exist
    println(whitespaceField.isBlank()) // true  — but it's all whitespace, useless

    // string templates — embed variables and expressions directly in strings
    // cleaner than concatenation with +
    val userName = "Daniel"
    val userScore = 42
    println("Welcome, $userName!")
    println("Score: $userScore, doubled: ${userScore * 2}")

    // string comparison — always use == in Kotlin, it compares value not memory location
    val name1 = "Saleh"
    val name2 = "Jamila"
    println(name1 == name2)                            // false
    println(name1.equals("saleh", ignoreCase = true))  // true — use for user input


    // ── ARITHMETIC OPERATORS ─────────────────────────

    val num1 = 10
    val num2 = 3
    println(num1 + num2)  // 13
    println(num1 - num2)  // 7
    println(num1 * num2)  // 30
    println(num1 / num2)  // 3 — Int division drops the decimal, 10/3 is not 3.33
    println(num1 % num2)  // 1 — remainder after division

    // % practical use: check if a number is even or odd
    println(10 % 2)  // 0 — even
    println(11 % 2)  // 1 — odd

    // if you need the decimal, at least one side must be a Double
    println(10.0 / 3)  // 3.3333...


    // ── KOTLIN.MATH ──────────────────────────────────
    // import kotlin.math.* at the top to use these without the prefix

    println(max(12, 10))   // 12 — pick the larger value
    println(min(12, 10))   // 10 — pick the smaller value
    println(abs(-5))       // 5  — remove the negative sign
    println(2.0.pow(8))    // 256.0 — 2 to the power of 8
    println(sqrt(144.0))   // 12.0  — square root
    println(round(4.6))    // 5.0   — nearest whole number
    println(floor(4.9))    // 4.0   — always round down
    println(ceil(4.1))     // 5.0   — always round up


    // ── INCREMENT / DECREMENT ────────────────────────

    var counter = 5
    println(counter++)  // 5 — prints current value, THEN increments
    println(counter--)  // 6 — prints current value, THEN decrements
    println(++counter)  // 6 — increments FIRST, then prints
    println(--counter)  // 5 — decrements FIRST, then prints

    // + behaves differently on strings vs numbers
    println(1 + 2)       // 3  — addition
    println("1" + "2")   // 12 — concatenation, glues text together


    // ── TYPE CONVERSION ──────────────────────────────
    // Kotlin never converts types automatically — you do it explicitly
    // this prevents silent bugs where "10" + 5 gives "105" instead of 15

    val numericString = "10"
    println(10 + numericString.toInt())  // 20 — converts string to Int first

    println(42.toString())               // "42" — Int to String
    println(9.99.toInt())                // 9   — drops decimal, does NOT round
    println(9.99.toLong())               // 9   — same, just Long type
    println("3.14".toDouble())           // 3.14

    // toIntOrNull — use this when the string might not be a valid number
    // returns null instead of crashing — essential for handling user input
    println("abc".toIntOrNull())         // null — no crash
    println("42".toIntOrNull())          // 42

    // real use: user types their age in a form field, you can't guarantee it's a number
    val ageInput = "twenty"
    val parsedAge = ageInput.toIntOrNull() ?: 0  // fallback to 0 if invalid
    println(parsedAge)  // 0


    // ── LOGICAL OPERATORS ────────────────────────────
    // && — AND: ALL conditions must be true
    // || — OR:  at least ONE condition must be true
    // !  — NOT: flips the value

    val isAdult = true
    val isFemale = false
    val isDriver = true

    println(isAdult && isDriver)               // true  — both true
    println(isAdult && (isFemale && isDriver)) // false — isFemale breaks it
    println(isFemale || isAdult)               // true  — isAdult saves it
    println(!isAdult)                          // false — flips true to false

    // real use: access control
    val hasPermission = isAdult && isDriver
    if (hasPermission) println("Access granted") else println("Access denied")


    // ── COMPARISON OPERATORS ─────────────────────────

    val n1 = 10
    val n2 = 20
    println(n1 > n2)   // false
    println(n1 < n2)   // true
    println(n1 >= n2)  // false
    println(n1 <= n2)  // true
    println(n1 == n2)  // false — value equality
    println(n1 != n2)  // true


    // ── IF / ELSE IF / ELSE ──────────────────────────

    val points = 75
    if (points >= 90) {
        println("A")
    } else if (points >= 70) {
        println("B")  // prints this
    } else {
        println("F")
    }

    // if as an expression — in Kotlin, if returns a value
    // this replaces the ternary operator (condition ? a : b) from Java
    val grade = if (points >= 70) "Pass" else "Fail"
    println(grade)  // Pass

    // real use: set a label based on a condition
    val statusLabel = if (isLoggedIn) "Active" else "Inactive"


    // ── WHEN EXPRESSION ──────────────────────────────
    // cleaner than a long chain of if/else if
    // like switch in Java but more powerful — it returns a value too

    val httpStatus = 404
    when (httpStatus) {
        200 -> println("OK")
        201 -> println("Created")
        400 -> println("Bad Request")
        401 -> println("Unauthorized")
        404 -> println("Not Found")   // prints this
        500 -> println("Server Error")
        else -> println("Unknown status")
    }

    // when as an expression
    val statusMessage = when (httpStatus) {
        200 -> "OK"
        404 -> "Not Found"
        500 -> "Server Error"
        else -> "Unknown"
    }
    println(statusMessage)  // Not Found

    // when with ranges
    val userAge = 18
    val ageGroup = when (userAge) {
        in 0..12  -> "Child"
        in 13..19 -> "Teenager"  // matches
        in 20..64 -> "Adult"
        else      -> "Senior"
    }
    println(ageGroup)  // Teenager

    // multiple values in one branch
    val dayOfWeek = 6
    val dayType = when (dayOfWeek) {
        1, 7    -> "Weekend"
        in 2..6 -> "Weekday"  // matches
        else    -> "Invalid"
    }
    println(dayType)  // Weekday


    // ── ARRAYS ───────────────────────────────────────
    // fixed size — you define the size upfront and cannot add or remove elements
    // in real Android and backend work you'll almost always use List instead
    // know arrays exist but don't reach for them first

    val usernames = arrayOf("Jamila", "James", "Daniel")
    println(usernames[0])                 // Jamila — access by index, starts at 0
    println(usernames.contentToString())  // [Jamila, James, Daniel]
    println(usernames.size)               // 3
    println(usernames.contains("James")) // true

    // typed arrays — more memory-efficient for primitive types
    val productIds = intArrayOf(101, 202, 303)

    // array constructor — generate values with a formula
    val squares = Array(5) { i -> i * i }
    println(squares.contentToString())  // [0, 1, 4, 9, 16]

    // arrayOfNulls — when you know the size but not the values yet
    val slots = arrayOfNulls<String>(3)
    slots[0] = "filled"
    println(slots.contentToString())  // [filled, null, null]


    // ── LISTS ────────────────────────────────────────
    // prefer List over Array — resizable (if mutable), more methods, more idiomatic Kotlin
    //
    // readonly list — cannot add, remove, or change after creation
    // use when the data is fixed: supported currencies, config values, nav destinations
    val supportedCountries: List<String> = listOf("Nigeria", "Ghana", "Kenya")
    println(supportedCountries[0])    // Nigeria
    println(supportedCountries.size)  // 3

    // mutable list — can add, remove, and update freely
    // use when building data over time: cart items, fetched posts, search results
    val cartItems = mutableListOf("Laptop", "Mouse")
    cartItems.add("Keyboard")
    cartItems.add(0, "Monitor")  // insert at index
    cartItems[1] = "Mechanical Keyboard"  // update
    cartItems.remove("Mouse")    // remove by value
    cartItems.removeAt(0)        // remove by index
    println(cartItems)

    // common methods — same on both List and MutableList
    val posts = listOf("Kotlin Basics", "Android Intro", "Ktor Setup", "Android Intro")
    println(posts.first())                  // Kotlin Basics
    println(posts.last())                   // Android Intro
    println(posts.indexOf("Android Intro")) // 1 — first occurrence
    println(posts.lastIndexOf("Android Intro")) // 3
    println(posts.isEmpty())               // false
    println(posts.reversed())              // reversed order
    println(posts.sorted())               // alphabetical
    println(posts.distinct())             // removes duplicates

    // destructuring — unpack list elements into named variables
    // _ skips an element you don't need
    val (first, second, _, fourth) = listOf("a", "b", "c", "d")
    println(first)   // a
    println(second)  // b
    println(fourth)  // d — "c" was skipped


    // ── FOR LOOP ─────────────────────────────────────
    // loop through any collection

    val notificationMessages = listOf("new message", "friend request", "payment received")

    for (message in notificationMessages) {
        // replaceFirstChar — capitalize first letter
        // the { it.uppercase() } part is a lambda — more on lambdas later
        val formatted = message.replaceFirstChar { it.uppercase() }
        println(formatted)
    }

    // loop with index — when you need the position too
    for ((index, message) in notificationMessages.withIndex()) {
        println("$index: $message")
    }

    // ranges
    for (i in 1..5) println(i)          // 1 2 3 4 5 — inclusive on both ends
    for (i in 1 until 5) println(i)     // 1 2 3 4   — excludes the end
    for (i in 5 downTo 1) println(i)    // 5 4 3 2 1 — countdown
    for (i in 0..10 step 2) println(i)  // 0 2 4 6 8 10 — every other


    // ── FOREACH ──────────────────────────────────────
    // cleaner than for loop when you just need each element
    // 'it' is the implicit name for the current element when there's one parameter

    notificationMessages.forEach { println(it) }

    // name it explicitly when the code is longer or less obvious
    notificationMessages.forEach { message ->
        println(message.uppercase())
    }

    // with index
    notificationMessages.forEachIndexed { index, message ->
        println("$index: $message")
    }

    // real backend use — process each record from a DB query
    val pendingOrders = listOf("order_001", "order_002", "order_003")
    pendingOrders.forEach { orderId ->
        println("Processing $orderId")  // would call DB or payment API here
    }


    // ── WHILE LOOP ───────────────────────────────────
    // use when you don't know how many iterations upfront
    // common in backend: polling for a result, retrying a failed network request

    var retries = 3
    while (retries > 0) {
        println("Attempting to connect... ($retries retries left)")
        retries--
    }
    // if it still fails after 3 tries, you'd throw an error here


    // ── DO WHILE ─────────────────────────────────────
    // block runs at least once before checking the condition
    // use when you need one guaranteed execution before the check

    var isDataReady = false
    do {
        println("Checking if data is ready...")
        isDataReady = true  // simulating data arriving
    } while (!isDataReady)


    // ── BREAK AND CONTINUE ───────────────────────────

    // break — exit the loop entirely when you've found what you need
    val allUserIds = listOf(101, 202, 303, 404, 505)
    val targetId = 303
    for (id in allUserIds) {
        if (id == targetId) {
            println("User found: $id")
            break  // no reason to keep looping
        }
    }

    // continue — skip the current item and move to the next
    // real use: skip bad or invalid data in a list before processing
    val rawPrices = listOf(9.99, -1.0, 4.99, -5.0, 14.99)
    for (price in rawPrices) {
        if (price < 0) continue  // skip negative prices — bad data
        println("Processing price: ₦$price")
    }


    // ── FUNCTIONS ────────────────────────────────────

    connectToDatabase()
    println(getUserById(42))
    registerUser(role = "admin", email = "dan@mail.com", username = "dan")
    fetchPosts()
    fetchPosts(page = 2)
    fetchPosts(page = 3, limit = 50)
    println(calculateOrderTotal(100.0, 0.075))   // 107.5
    println(isUserLoggedIn(null))                 // false
    println(isUserLoggedIn("token_abc123"))       // true
    println(validateLogin("", "pass"))            // Email is required
    println(validateLogin("a@b.com", "123"))      // Password too short
    println(validateLogin("a@b.com", "123456"))   // Login successful
    makeApiRequest { println("Data loaded, update the UI now") }
    println(formatToNaira(1500.0))                // ₦1500.0


    // ── CLASSES — CREATING OBJECTS ───────────────────

    // class is a blueprint — User is the blueprint
    // object is the actual thing created from that blueprint — user is the object
    val user = User(id = 1, email = "dan@mail.com", password = "secret123")
    println(user.email)     // dan@mail.com
    println(user.isActive)  // true — default value
    println(user)           // User(id=1, email=dan@mail.com) — from toString()

    user.deactivate()
    println(user.isActive)  // false

    // getters and setters
    // every property automatically has a getter (read it) and setter (write to it)
    // you only write a custom one when you need logic on read or write
    println(user.displayEmail)  // [1] dan@mail.com — computed on every access
    user.score = -10            // setter intercepts and rejects the negative value
    println(user.score)         // 0 — setter kept 0 instead of storing -10

    // companion object — call without creating an instance, like a static method in Java
    val guest = User.createGuest()
    println(guest.email)  // guest@app.com


    // ── DATA CLASS ───────────────────────────────────
    // designed purely to hold data — like a row from the DB or a JSON response body
    // gives you toString(), equals(), copy(), hashCode() automatically
    // use for: LoginRequest, UserResponse, PostDto, any model class

    val post = Post(id = 10, title = "Kotlin Notes", body = "Clean and practical")
    println(post)            // Post(id=10, title=Kotlin Notes, body=Clean and practical)
    println(post.summary())  // [10] Kotlin Notes

    // copy — create a new object with some fields changed, original stays untouched
    // real use: update a record without mutating the original
    val updatedPost = post.copy(title = "Kotlin Notes — Updated")
    println(updatedPost.title)  // Kotlin Notes — Updated
    println(post.title)         // Kotlin Notes — unchanged

    // data classes compare by value, not by memory location
    val post2 = Post(id = 10, title = "Kotlin Notes", body = "Clean and practical")
    println(post == post2)   // true  — same values
    println(post === post2)  // false — different objects in memory


    // ── ENUM CLASS ───────────────────────────────────
    // a fixed set of named constants
    // safer than scattering raw strings like "ADMIN" all over your code
    // if you typo "ADMON" the compiler catches it — a string wouldn't
    // real use: user roles, order statuses, navigation routes, HTTP methods

    val currentUserRole = UserRole.ADMIN
    when (currentUserRole) {
        UserRole.ADMIN  -> println("Full access — can do everything")
        UserRole.EDITOR -> println("Can create and edit content")
        UserRole.VIEWER -> println("Read only access")
    }
    println(currentUserRole.label)  // Administrator — custom property on the enum


    // ── SEALED CLASS ─────────────────────────────────
    // a fixed set of types grouped under one parent
    // the compiler knows every possible type — 'when' forces you to handle all of them
    // perfect for representing the state of an API call: loading, success, or error
    // you'll use this constantly in Android ViewModels

    val apiResult: ApiResponse = ApiResponse.Success(data = "User profile loaded")
    when (apiResult) {
        is ApiResponse.Success -> println("Show data: ${apiResult.data}")
        is ApiResponse.Error   -> println("Show error: ${apiResult.message} (${apiResult.code})")
        is ApiResponse.Loading -> println("Show loading spinner")
    }


    // ── OBJECT — SINGLETON ───────────────────────────
    // the object keyword creates a class that has exactly ONE instance ever
    // Kotlin creates it the first time it's accessed and reuses it forever
    // real use: database connection pool, HTTP client, app-wide config
    // you don't want 50 different database connections — you want one shared one

    DatabaseConfig.connect()
    println(DatabaseConfig.url)           // jdbc:postgresql://localhost:5432/mydb
    println(DatabaseConfig.maxConnections) // 10


    // ── INTERFACE ────────────────────────────────────
    // defines a contract — any class that implements it MUST provide these functions
    // your code talks to the interface, not the specific class
    // this means you can swap implementations without changing anything else
    //
    // real use in Android: you write your ViewModel against a UserRepository interface
    // in production it talks to the real DB, in tests you swap in a fake one
    // in Ktor/Spring Boot: swap EmailService for SmsService or MockService easily

    val emailService: NotificationService = EmailNotificationService()
    emailService.send(to = "dan@mail.com", message = "Welcome to the app!")

    val smsService: NotificationService = SmsNotificationService()
    smsService.send(to = "+2348010000000", message = "Your OTP is 4521")

    // both called the same way — same interface, different behaviour
    // this is polymorphism too


    // ── ABSTRACT CLASS ───────────────────────────────
    // sits between interface and open class
    // interface: only contracts (and optional defaults), no stored state
    // abstract class: contracts you must fill + shared real code + can store state
    // use when subclasses share actual logic but also need their own specific behaviour

    val userRepo = UserRepository()
    userRepo.save("dan@mail.com")
    // Saving to database...  ← from BaseRepository (shared logic)
    // Saved user: dan@mail.com  ← from UserRepository (specific logic)

    userRepo.delete("dan@mail.com")
    // Deleting from database...
    // Deleted user: dan@mail.com


    // ── INHERITANCE AND POLYMORPHISM ─────────────────
    // inheritance: a child class gets all properties and functions of the parent
    // classes in Kotlin are final by default — add 'open' to allow extending
    //
    // polymorphism: one type, many forms
    // you can hold different child objects under the parent type
    // and call the same function — each one responds differently
    //
    // real use in Android: Activity, Fragment, ViewModel are all parent classes
    // your LoginActivity extends AppCompatActivity — that's inheritance
    // Ktor and Spring Boot route handlers all implement the same interface — polymorphism

    val bankAccounts: List<Account> = listOf(
        SavingsAccount(owner = "Daniel", balance = 50000.0),
        BusinessAccount(owner = "Saleh", balance = 200000.0)
    )

    // same function call on every account — each behaves differently
    // that's polymorphism in action
    for (account in bankAccounts) {
        println(account.getAccountInfo())
    }
    // Savings  | Daniel | Balance after fee: ₦45000.0
    // Business | Saleh  | Balance after fee: ₦197000.0


    // ── EXTENSION FUNCTIONS ──────────────────────────
    // add a new function to an existing class without modifying the class itself
    // 'this' inside the function refers to the object it was called on
    //
    // real use in Android: String.isValidEmail(), View.hide(), Context.showToast()
    // you'll write these constantly to keep your code clean and readable

    println("dan@mail.com".isValidEmail())   // true
    println("not-an-email".isValidEmail())   // false
    println("hello world".capitalizeWords()) // Hello World
    println(1500.0.toNaira())                // ₦1500.0

    // extension functions on nullable types
    // real use: safely call on something that might be null
    val nullableEmail: String? = null
    println(nullableEmail.isNullOrEmpty())  // true — built-in extension on String?


    // ── LAMBDA FUNCTIONS ─────────────────────────────
    // a function with no name, written inline
    // assigned to a variable or passed directly to another function
    // syntax: { parameter -> body }
    // type:   (InputType) -> ReturnType

    val double: (Int) -> Int = { number -> number * 2 }
    println(double(5))  // 10

    // when there's only one parameter, drop it and use 'it'
    val triple: (Int) -> Int = { it * 3 }
    println(triple(5))  // 15

    val addNumbers: (Int, Int) -> Int = { a, b -> a + b }
    println(addNumbers(3, 4))  // 7

    val logEvent: (String) -> Unit = { message -> println("LOG: $message") }
    logEvent("User logged in")  // LOG: User logged in

    // you've already been using lambdas — these { } blocks are all lambdas:
    // notificationMessages.forEach { println(it) }
    // rawPrices.filter { it > 0 }
    // message.replaceFirstChar { it.uppercase() }

    // higher order functions — built-in functions that take lambdas
    // these are some of the most useful tools in Kotlin

    val userList = listOf("daniel", "saleh", "jamila", "dan")

    // filter — keep only items where the condition is true
    val longUsernames = userList.filter { it.length > 4 }
    println(longUsernames)  // [daniel, saleh, jamila]

    // map — transform every item into something else, returns a new list
    val uppercasedNames = userList.map { it.uppercase() }
    println(uppercasedNames)  // [DANIEL, SALEH, JAMILA, DAN]

    // chain them — filter first, then transform
    val formattedNames = userList
        .filter { it.length > 3 }
        .map { it.replaceFirstChar { c -> c.uppercase() } }
    println(formattedNames)  // [Daniel, Saleh, Jamila]

    // real backend use — map DB rows to API response objects
    // never expose internal data like password hashes to the client
    val dbRows = listOf(
        UserEntity(1, "dan@mail.com", "hashed_password_123"),
        UserEntity(2, "saleh@mail.com", "hashed_password_456")
    )
    val responseBody = dbRows.map { UserResponse(id = it.id, email = it.email) }
    println(responseBody)  // [UserResponse(id=1, ...), UserResponse(id=2, ...)]


    // ── SEALED INTERFACE ─────────────────────────────
    // like a sealed class but a class can implement multiple sealed interfaces
    // a class can only extend one parent class — sealed or not
    //
    // use sealed interface when your types need to implement other interfaces too
    // most common in Android for UI state and in Ktor/Spring Boot for result wrappers

    val fetchResult: Result<String> = Result.Success("User data fetched")
    handleResult(fetchResult)  // Data: User data fetched

    val errorResult: Result<String> = Result.Error("Not found", code = 404)
    handleResult(errorResult)  // Error 404: Not found

    handleResult(Result.Loading)  // Loading...

    // sealed interface vs sealed class
    // sealed class     → simpler, one parent only, use this most of the time
    // sealed interface → more flexible, implement multiple interfaces, use when needed

}


// ─────────────────────────────────────────────────
// FUNCTIONS
// ─────────────────────────────────────────────────
// defined outside main
// public by default — no need to write 'public'
// returns Unit by default — Unit means nothing is returned (like void in Java)

fun connectToDatabase() {
    println("Connecting to database...")
}

// return type declared after the parentheses
fun getUserById(id: Int): String {
    return "Fetching user $id from database"
}

// named arguments — caller can pass in any order, makes the call self-documenting
fun registerUser(username: String, email: String, role: String) {
    println("Registering $username as $role with email $email")
}

// default arguments — parameter gets a fallback value if caller doesn't pass one
// real use: paginated API endpoints where page=1 and limit=20 are sensible defaults
fun fetchPosts(page: Int = 1, limit: Int = 20) {
    println("Fetching page $page, limit $limit")
}

// single expression function — drop { } and return when the body is one expression
fun calculateOrderTotal(price: Double, taxRate: Double): Double = price + (price * taxRate)

fun isUserLoggedIn(token: String?) = token != null

// return as early exit — validate first, do the real work only if everything passes
// keeps the happy path (successful case) at the bottom, easy to read
fun validateLogin(email: String, password: String): String {
    if (email.isBlank()) return "Email is required"
    if (password.length < 6) return "Password too short"
    return "Login successful"
}

// function as argument — accepts a lambda as a parameter
// () -> Unit means: a function that takes no arguments and returns nothing
// real use in Android: onClick handlers, Compose UI events, network callbacks
fun makeApiRequest(onSuccess: () -> Unit) {
    println("Request sent...")
    onSuccess()  // call the lambda that was passed in
}

// lambda stored in a variable
val formatToNaira = { amount: Double -> "₦$amount" }


// ─────────────────────────────────────────────────
// CLASSES
// ─────────────────────────────────────────────────

// class = blueprint   object = the actual thing created from the blueprint
//
// primary constructor: parameters go in the class header
// val/var in the constructor = property stored on the object
// without val/var = just a constructor parameter, not accessible outside init

class User(
    val id: Int,
    val email: String,
    private val password: String,  // private — only this class can access it
    var isActive: Boolean = true   // default value
) {
    // init block — runs the moment the object is created
    // use for validation so a User with an invalid email can never exist
    init {
        require(email.contains("@")) { "Invalid email: $email" }
        require(password.length >= 6) { "Password must be at least 6 characters" }
    }

    // custom getter — computed on every access, nothing stored
    // real use: format data differently for display without storing the formatted version
    val displayEmail: String
        get() = "[$id] $email"

    // custom setter — intercepts the value before storing it
    // field = the actual backing storage for this property
    var score: Int = 0
        set(value) {
            field = if (value >= 0) value else 0  // reject negatives
        }

    fun deactivate() {
        isActive = false
    }

    // toString — controls what you see when you println(user) or log the object
    // without override you'd see something like: User@3764951d (useless)
    override fun toString() = "User(id=$id, email=$email)"

    // companion object — belongs to the class itself, not to any instance
    // call it as User.createGuest(), no object needed — similar to static in Java
    // real use: factory methods, constants tied to the class
    companion object {
        fun createGuest() = User(id = 0, email = "guest@app.com", password = "guest00")
    }
}


// data class — designed to hold data, nothing more
// auto-generates: toString(), equals(), hashCode(), copy()
// use for every model: LoginRequest, UserResponse, PostDto, OrderItem
data class Post(
    val id: Int,
    val title: String,
    val body: String
) {
    fun summary() = "[$id] $title"
}


// ── ENUM CLASS ───────────────────────────────────
// fixed set of named constants, each can carry its own properties
// real use: user roles, order status, HTTP methods, navigation routes
enum class UserRole(val label: String) {
    ADMIN("Administrator"),
    EDITOR("Editor"),
    VIEWER("Viewer")
}


// ── SEALED CLASS ─────────────────────────────────
// fixed set of subclasses, all in the same file
// the compiler knows every possible type — 'when' is exhaustive
// if you add a new subclass and forget to handle it, the compiler tells you
sealed class ApiResponse {
    data class Success(val data: String) : ApiResponse()
    data class Error(val message: String, val code: Int = 500) : ApiResponse()
    object Loading : ApiResponse()  // object = single instance, no data needed
}


// ── OBJECT — SINGLETON ───────────────────────────
// Kotlin creates exactly one instance, the first time it's accessed
// all access after that returns the same instance
object DatabaseConfig {
    val url = "jdbc:postgresql://localhost:5432/mydb"
    val maxConnections = 10

    fun connect() {
        println("Connected to $url")
    }
}


// ── INTERFACE ────────────────────────────────────
// defines what a class must do, not how it does it
// your code depends on the interface — you can swap the implementation anytime
// this is the foundation of dependency injection and testable architecture

interface NotificationService {
    fun send(to: String, message: String)
    fun isAvailable(): Boolean = true  // default implementation — override if needed
}

class EmailNotificationService : NotificationService {
    override fun send(to: String, message: String) {
        println("Email → $to: $message")
    }
}

class SmsNotificationService : NotificationService {
    override fun send(to: String, message: String) {
        println("SMS → $to: $message")
    }
}


// ── ABSTRACT CLASS ───────────────────────────────
// cannot be instantiated directly — only its subclasses can
// concrete functions (with bodies) are shared by all subclasses
// abstract functions (no body) must be implemented by each subclass
abstract class BaseRepository {
    fun save(data: String) {
        println("Saving to database...")
        persist(data)  // calls the subclass-specific implementation
    }

    fun delete(data: String) {
        println("Deleting from database...")
        remove(data)
    }

    abstract fun persist(data: String)
    abstract fun remove(data: String)
}

class UserRepository : BaseRepository() {
    override fun persist(data: String) { println("Saved user: $data") }
    override fun remove(data: String) { println("Deleted user: $data") }
}


// ── INHERITANCE AND POLYMORPHISM ─────────────────
// open = this class is allowed to be extended (Kotlin classes are final by default)
// override = replace the parent's version with your own

open class Account(
    val owner: String,
    val balance: Double
) {
    open fun getAccountInfo(): String = "Account | $owner | ₦$balance"
}

class SavingsAccount(owner: String, balance: Double) : Account(owner, balance) {
    private val feePercent = 0.10

    override fun getAccountInfo(): String {
        val afterFee = balance - (balance * feePercent)
        return "Savings  | $owner | Balance after fee: ₦$afterFee"
    }
}

class BusinessAccount(owner: String, balance: Double) : Account(owner, balance) {
    private val flatFee = 3000.0

    override fun getAccountInfo(): String {
        val afterFee = balance - flatFee
        return "Business | $owner | Balance after fee: ₦$afterFee"
    }
}


// ── VISIBILITY MODIFIERS ─────────────────────────
// public    → accessible from anywhere (default, no need to write it)
// private   → only inside this class or file
// protected → this class and any subclass
// internal  → anywhere within the same module (same app or library)


// ── EXTENSION FUNCTIONS ──────────────────────────
// add a function to an existing class without touching the class source code
// 'this' = the object the function is called on (the receiver)

fun String.isValidEmail(): Boolean = this.contains("@") && this.contains(".")

fun String.capitalizeWords(): String =
    this.split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

fun Double.toNaira(): String = "₦$this"


// ── LAMBDA FUNCTIONS — SUPPORTING CLASSES ────────
// these data classes are used in the lambda section inside main

data class UserEntity(val id: Int, val email: String, val passwordHash: String)
data class UserResponse(val id: Int, val email: String)


// ── SEALED INTERFACE ─────────────────────────────
// like sealed class but a class can implement multiple sealed interfaces
// a class can only extend one parent — sealed or not
// use when your result types also need to implement other interfaces

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val message: String, val code: Int = 500) : Result<Nothing>
    object Loading : Result<Nothing>
}

fun handleResult(result: Result<String>) {
    when (result) {
        is Result.Success -> println("Data: ${result.data}")
        is Result.Error   -> println("Error ${result.code}: ${result.message}")
        is Result.Loading -> println("Loading...")
    }
}