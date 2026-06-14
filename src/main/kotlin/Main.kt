//const val PI = 3.14  // const defined outside main, value must be known before execution
import kotlin.math.*

fun main() {

    // ── VARIABLES ────────────────────────────────────
    // var: mutable — can be reassigned
    // val: immutable — cannot be reassigned
    // const: like val but defined outside main, evaluated at compile time

    var name: String = "Emmanuel"
    var age: Int = 19
    println("Name: $name, Age: $age")


    // ── DATA TYPES ───────────────────────────────────

    var n: Int = 10
    var l: Long = 100L
    var d: Double = 3.14
    var f: Float = 3.14F
    var b: Boolean = true
    var s: String = "Kakashi"
    var c: Char = 'A'
    var a: Any = "10"  // Any can hold any data type — avoid in practice, loses type safety

    // type inference — Kotlin figures out the type from the value
    var city = "Lagos"   // inferred as String
    var score = 100      // inferred as Int


    // ── NULL SAFETY ──────────────────────────────────
    // String  → cannot be null, compile error if you try
    // String? → allowed to be null

    var brand: String? = null

    // ?. safe call — skips method if null, returns null instead of crashing
    println(brand?.uppercase())  // null

    brand = "kotlin"
    println(brand?.uppercase())  // KOTLIN

    // ?: elvis — fallback value when left side is null
    val displayName = brand?.uppercase() ?: "UNKNOWN BRAND"
    println(displayName)  // KOTLIN

    // ?.let — runs block only if not null, 'it' = the non-null value
    brand?.let {
        println(it.uppercase())  // KOTLIN
        println(it.length)       // 6
    }

    // Boolean? has 3 states: true, false, null
    val isVerified: Boolean? = null
    if (isVerified == true) {
        println("Verified")
    } else {
        println("Not verified or unknown")  // prints this
    }


    // ── STRINGS ──────────────────────────────────────

    val email = "   user@gmail.com   "
    println(email.trim())       // user@gmail.com — always trim user input
    println(email.length)       // 22

    val username = "dan"
    if (username.length < 5) println("username too short")

    val role = "ADMIN"
    println(role.lowercase())   // admin
    println(role.uppercase())   // ADMIN

    if ("admin".equals(role, ignoreCase = true)) println("is admin")

    val bio = "Android developer from Abuja"
    println(bio.contains("Android"))                     // true
    println(bio.contains("android", ignoreCase = true))  // true

    val dirty = "+234-801-000-0000"
    println(dirty.replace("-", ""))  // +2348010000000

    val csv = "name,age,city"
    val fields = csv.split(",")
    println(fields[0])  // name

    val date = "2025-06-13"
    println(date.substring(0, 4))  // 2025

    val url = "https://api.example.com/users"
    println(url.startsWith("https"))  // true
    println(url.endsWith("/users"))   // true

    val blankInput = "     "
    println(blankInput.isEmpty())  // false — has space characters
    println(blankInput.isBlank())  // true  — prefer this for input validation

    val userName = "Daniel"
    println("Welcome, $userName!")
    println("Username has ${userName.length} characters.")

    val name1 = "Saleh"
    val name2 = "Jamila"
    println(name1 == name2)                            // false
    println(name1.equals("saleh", ignoreCase = true))  // true


    // ── ARITHMETIC OPERATORS ─────────────────────────

    val num1 = 10
    val num2 = 3
    println(num1 + num2)  // 13
    println(num1 - num2)  // 7
    println(num1 * num2)  // 30
    println(num1 / num2)  // 3 — Int division drops decimal
    println(num1 % num2)  // 1 — remainder, use to check even/odd


    // ── KOTLIN.MATH ──────────────────────────────────

    println(max(12, 10))   // 12
    println(min(12, 10))   // 10
    println(abs(-5))       // 5
    println(2.0.pow(8))    // 256.0
    println(sqrt(144.0))   // 12.0
    println(round(4.6))    // 5.0
    println(floor(4.9))    // 4.0
    println(ceil(4.1))     // 5.0


    // ── INCREMENT / DECREMENT ────────────────────────

    var count = 5
    println(count++)  // 5 — prints then increments
    println(count--)  // 6 — prints then decrements
    println(++count)  // 6 — increments then prints
    println(--count)  // 5 — decrements then prints

    println("1" + "2")  // 12 — string concatenation
    println(1 + 2)       // 3  — addition


    // ── TYPE CONVERSION ──────────────────────────────

    val no = "10"
    println(10 + no.toInt())     // 20
    println(42.toString())        // "42"
    println(9.99.toInt())         // 9 — drops decimal, does not round
    println("abc".toIntOrNull())  // null — safe, won't crash


    // ── LOGICAL OPERATORS ────────────────────────────

    val isAdult = true
    val isFemale = false
    val isDriver = true

    println(isAdult && (isFemale && isDriver))  // false — ALL must be true
    println(isFemale || isAdult)                // true  — at least ONE must be true
    println(!isAdult)                           // false — flips the value


    // ── COMPARISON OPERATORS ─────────────────────────

    val n1 = 10
    val n2 = 20
    println(n1 > n2)   // false
    println(n1 < n2)   // true
    println(n1 >= n2)  // false
    println(n1 <= n2)  // true
    println(n1 == n2)  // false
    println(n1 != n2)  // true


    // ── IF / ELSE IF / ELSE ──────────────────────────

    val userScore = 75
    if (userScore >= 90) {
        println("A")
    } else if (userScore >= 70) {
        println("B")  // prints this
    } else {
        println("F")
    }

    // if as an expression — returns a value directly
    val grade = if (userScore >= 70) "Pass" else "Fail"
    println(grade)  // Pass


    // ── WHEN EXPRESSION ──────────────────────────────

    val gender = 'F'
    when (gender) {
        'F' -> println("Female")
        'M' -> println("Male")
        else -> println("Unknown")
    }

    val genderLabel = when (gender) {
        'F' -> "Female"
        'M' -> "Male"
        else -> "Unknown"
    }
    println(genderLabel)  // Female

    val userAge = 18
    when (userAge) {
        in 0..12  -> println("Child")
        in 13..19 -> println("Teenager")  // prints this
        in 20..64 -> println("Adult")
        else      -> println("Senior")
    }

    val day = 6
    when (day) {
        1, 7    -> println("Weekend")
        in 2..6 -> println("Weekday")  // prints this
        else    -> println("Invalid")
    }


    // ── ARRAYS ───────────────────────────────────────
    // fixed size — cannot add or remove elements after creation

    val usernames = arrayOf("Jamila", "James", "Daniel")
    println(usernames[0])                 // Jamila
    println(usernames.contentToString())  // [Jamila, James, Daniel]
    println(usernames.size)               // 3
    println(usernames.contains("James"))  // true

    val ids = intArrayOf(101, 202, 303)   // typed array — more efficient for primitives

    val squares = Array(5) { i -> i * i } // builds with a formula
    println(squares.contentToString())    // [0, 1, 4, 9, 16]

    val slots = arrayOfNulls<String>(3)   // fixed size, slots start as null
    slots[0] = "filled"
    println(slots.contentToString())      // [filled, null, null]


    // ── LISTS ────────────────────────────────────────

    // readonly — use for fixed data: config values, dropdown options, API responses
    val countries: List<String> = listOf("Nigeria", "Ghana", "Kenya")
    println(countries[0])    // Nigeria
    println(countries.size)  // 3

    // mutable — use when building data over time: cart items, search results, a feed
    val cities = mutableListOf("Lagos", "Accra")
    cities.add("Nairobi")
    cities.add(0, "Abuja")
    cities[1] = "Kano"
    cities.remove("Accra")
    cities.removeAt(0)
    println(cities)  // [Kano, Nairobi]

    val items = listOf("Mango", "Apple", "Banana", "Apple")
    println(items.first())               // Mango
    println(items.last())                // Banana
    println(items.indexOf("Apple"))      // 1
    println(items.lastIndexOf("Apple"))  // 3
    println(items.reversed())            // [Apple, Banana, Apple, Mango]
    println(items.sorted())              // [Apple, Apple, Banana, Mango]
    println(items.distinct())            // [Mango, Apple, Banana]

    // destructuring — unpack elements into variables, _ skips one
    val (one, two, _, three) = listOf("a", "b", "c", "d")
    println(one)    // a
    println(two)    // b
    println(three)  // d


    // ── FOR LOOP ─────────────────────────────────────

    val notifications = listOf("new message", "friend request", "payment received")

    for (notification in notifications) {
        val formatted = notification.replaceFirstChar { it.uppercase() }
        println(formatted)
    }

    for ((index, notification) in notifications.withIndex()) {
        println("$index: $notification")
    }

    for (i in 1..5) println(i)          // 1 2 3 4 5
    for (i in 1 until 5) println(i)     // 1 2 3 4
    for (i in 5 downTo 1) println(i)    // 5 4 3 2 1
    for (i in 1..10 step 2) println(i)  // 1 3 5 7 9


    // ── FOREACH ──────────────────────────────────────

    notifications.forEach { println(it) }
    notifications.forEachIndexed { index, item -> println("$index: $item") }

    val orders = listOf("order_001", "order_002", "order_003")
    orders.forEach { orderId -> println("Processing $orderId") }


    // ── WHILE / DO WHILE ─────────────────────────────

    var retries = 3
    while (retries > 0) {
        println("Attempting connection... ($retries left)")
        retries--
    }

    var dataLoaded = false
    do {
        println("Loading...")
        dataLoaded = true
    } while (!dataLoaded)


    // ── BREAK AND CONTINUE ───────────────────────────

    val userIds = listOf(101, 202, 303, 404)
    for (id in userIds) {
        if (id == 303) {
            println("User found: $id")
            break
        }
    }

    val prices = listOf(9.99, -1.0, 4.99, -5.0, 14.99)
    for (price in prices) {
        if (price < 0) continue
        println("Valid price: $price")
    }


    // ── FUNCTIONS ────────────────────────────────────

    connectToDatabase()
    println(getUserById(42))
    registerUser(role = "admin", email = "dan@mail.com", username = "dan")
    fetchPosts()
    fetchPosts(page = 2)
    println(calculateTotal(100.0, 0.075))
    println(isLoggedIn(null))               // false
    println(isLoggedIn("token_abc"))        // true
    println(login("", "pass"))              // Email is required
    println(login("a@b.com", "123456"))     // Login successful
    performRequest { println("Data loaded") }
    println(formatPrice(1500.0))            // ₦1500.0


    // ── CLASSES — CREATING OBJECTS ───────────────────

    val user = User(id = 1, email = "dan@mail.com", password = "secret123")
    println(user.email)      // dan@mail.com
    println(user.isActive)   // true
    println(user)            // User(id=1, email=dan@mail.com) — from toString()
    user.deactivate()
    println(user.isActive)   // false

    // getters and setters
    // by default every property has a getter (read) and setter (write) generated
    // you only write a custom one when you need extra logic on get or set
    println(user.displayEmail)   // [1] dan@mail.com — custom getter
    user.score = -10             // custom setter blocks negative values
    println(user.score)          // 0 — setter rejected -10, kept 0

    // companion object — call without creating an instance
    val guest = User.createGuest()
    println(guest.email)  // guest@app.com


    // ── DATA CLASS ───────────────────────────────────
    // toString(), equals(), copy() all generated automatically
    // use for API request/response bodies and DB models

    val post = Post(id = 10, title = "Kotlin Classes", body = "Clean and simple")
    println(post)                // Post(id=10, title=Kotlin Classes, body=Clean and simple)
    println(post.summary())      // [10] Kotlin Classes

    val updatedPost = post.copy(title = "Kotlin Classes Updated")
    println(updatedPost.title)   // Kotlin Classes Updated
    println(post.title)          // Kotlin Classes — original unchanged

    // equality — data classes compare by value not reference
    val post2 = Post(id = 10, title = "Kotlin Classes", body = "Clean and simple")
    println(post == post2)   // true — same values
    println(post === post2)  // false — different objects in memory


    // ── ENUM CLASS ───────────────────────────────────
    // fixed set of constants — use for roles, statuses, navigation routes
    // safer than using raw strings like "ADMIN" scattered across your code

    val userRole = UserRole.ADMIN
    when (userRole) {
        UserRole.ADMIN  -> println("Full access")
        UserRole.EDITOR -> println("Can edit content")
        UserRole.VIEWER -> println("Read only")
    }

    println(userRole.label)  // Administrator — custom property on enum


    // ── SEALED CLASS ─────────────────────────────────
    // fixed set of types under one parent
    // perfect for API states — when you use 'when', Kotlin forces every case handled

    val response: ApiResponse = ApiResponse.Success(data = "User created")
    when (response) {
        is ApiResponse.Success -> println("Got: ${response.data}")
        is ApiResponse.Error   -> println("Failed: ${response.message}")
        is ApiResponse.Loading -> println("Loading...")
    }


    // ── OBJECT — SINGLETON ───────────────────────────
    // object keyword creates a class with exactly one instance ever
    // use for database connections, API clients, app-wide config

    DatabaseConfig.connect()
    println(DatabaseConfig.url)  // jdbc:postgresql://localhost:5432/mydb


    // ── INTERFACE ────────────────────────────────────
    // defines a contract — implementing class must provide the functions
    // lets you swap implementations without changing other code
    // core to dependency injection in Spring Boot and Ktor

    val emailService: NotificationService = EmailNotificationService()
    emailService.send(to = "dan@mail.com", message = "Welcome!")

    val smsService: NotificationService = SmsNotificationService()
    smsService.send(to = "+2348010000000", message = "Your OTP is 4521")


    // ── ABSTRACT CLASS ───────────────────────────────
    // sits between interface and open class
    // can have real implemented functions AND abstract ones that subclasses must fill in
    // use when subclasses share some logic but each must define their own behaviour

    val repo = UserRepository()
    repo.save("dan@mail.com")   // Saving to database... / Saved user: dan@mail.com
    repo.delete("dan@mail.com") // Deleting from database... / Deleted user: dan@mail.com


    // ── INHERITANCE AND POLYMORPHISM ─────────────────
    // polymorphism: one type, many forms
    // you can hold different objects under the same parent type
    // and call the same function — each behaves differently

    val accounts: List<Account> = listOf(
        SavingsAccount(owner = "Daniel", balance = 50000.0),
        BusinessAccount(owner = "Saleh", balance = 200000.0)
    )

    // same function call, different behaviour per type — that's polymorphism
    for (account in accounts) {
        println(account.getAccountInfo())
    }
    // Savings | Daniel | Balance: 45000.0  (after 10% fee)
    // Business | Saleh | Balance: 197000.0 (after flat 3000 fee)

}


// ─────────────────────────────────────────────────
// FUNCTIONS
// ─────────────────────────────────────────────────

fun connectToDatabase() { println("Connecting to database...") }

fun getUserById(id: Int): String = "Fetching user $id from database"

fun registerUser(username: String, email: String, role: String) {
    println("Registering $username as $role")
}

fun fetchPosts(page: Int = 1, limit: Int = 20) {
    println("Fetching page $page with $limit posts")
}

fun calculateTotal(price: Double, taxRate: Double): Double = price + (price * taxRate)

fun isLoggedIn(token: String?) = token != null

fun login(email: String, password: String): String {
    if (email.isBlank()) return "Email is required"
    if (password.length < 6) return "Password too short"
    return "Login successful"
}

fun performRequest(onSuccess: () -> Unit) {
    println("Request sent")
    onSuccess()
}

val formatPrice = { amount: Double -> "₦$amount" }


// ─────────────────────────────────────────────────
// CLASSES
// ─────────────────────────────────────────────────

// primary constructor — val/var in the header = property on the class
// without val/var it's just a constructor parameter, not a property
// init block — runs when the object is created, good for validation

class User(
    val id: Int,
    val email: String,
    private val password: String,  // private — only accessible inside this class
    var isActive: Boolean = true
) {
    // init block runs immediately when object is created
    init {
        require(email.contains("@")) { "Invalid email format" }
        require(password.length >= 6) { "Password must be at least 6 characters" }
    }

    // custom getter — computed property, no value stored, calculated on access
    val displayEmail: String
        get() = "[$id] $email"

    // custom setter — validate before storing
    var score: Int = 0
        set(value) {
            field = if (value >= 0) value else 0  // field = the actual stored value
        }

    fun deactivate() { isActive = false }

    // toString — controls what prints when you println(user)
    // without this you'd get something like User@1b6d3586
    override fun toString() = "User(id=$id, email=$email)"

    // companion object — belongs to the class, not instances
    // call with User.createGuest(), no object needed
    companion object {
        fun createGuest() = User(id = 0, email = "guest@app.com", password = "guest00")
    }
}


// data class — designed to hold data
// auto-generates: toString(), equals(), hashCode(), copy()
// use for API models: LoginRequest, UserResponse, PostDto
data class Post(
    val id: Int,
    val title: String,
    val body: String
) {
    fun summary() = "[$id] $title"
}


// ── ENUM CLASS ───────────────────────────────────
// each constant can have its own properties
enum class UserRole(val label: String) {
    ADMIN("Administrator"),
    EDITOR("Editor"),
    VIEWER("Viewer")
}


// ── SEALED CLASS ─────────────────────────────────
// all subclasses must be in the same file
// 'when' on a sealed class is exhaustive — compiler tells you if you miss a case
sealed class ApiResponse {
    data class Success(val data: String) : ApiResponse()
    data class Error(val message: String, val code: Int = 500) : ApiResponse()
    object Loading : ApiResponse()  // object = single instance, no data needed
}


// ── OBJECT — SINGLETON ───────────────────────────
// only one instance of this ever exists — useful for shared resources
object DatabaseConfig {
    val url = "jdbc:postgresql://localhost:5432/mydb"
    val maxConnections = 10

    fun connect() {
        println("Connected to $url")
    }
}


// ── INTERFACE ────────────────────────────────────
// a contract — implementing class provides the actual behaviour
// your code depends on the interface, not the implementation
// this is how you write testable, swappable code

interface NotificationService {
    fun send(to: String, message: String)
    fun isAvailable(): Boolean = true  // interfaces can have default implementations
}

class EmailNotificationService : NotificationService {
    override fun send(to: String, message: String) {
        println("Sending email to $to: $message")
    }
}

class SmsNotificationService : NotificationService {
    override fun send(to: String, message: String) {
        println("Sending SMS to $to: $message")
    }
}


// ── ABSTRACT CLASS ───────────────────────────────
// like an interface but can have real implemented functions too
// use when subclasses share some logic but must define specific behaviour themselves
// cannot be instantiated directly — must be subclassed

abstract class BaseRepository {
    // concrete function — shared by all subclasses
    fun save(data: String) {
        println("Saving to database...")
        persist(data)
    }

    fun delete(data: String) {
        println("Deleting from database...")
        remove(data)
    }

    // abstract — subclass MUST implement these
    abstract fun persist(data: String)
    abstract fun remove(data: String)
}

class UserRepository : BaseRepository() {
    override fun persist(data: String) { println("Saved user: $data") }
    override fun remove(data: String) { println("Deleted user: $data") }
}


// ── INHERITANCE AND POLYMORPHISM ─────────────────
// open = this class can be extended (classes are final by default in Kotlin)
// override = replace the parent's version of a function
//
// polymorphism: treat different types as the same parent type
// call the same function on each — each responds differently
// this is how Android handles different screen types, Ktor handles different routes

open class Account(
    val owner: String,
    var balance: Double
) {
    open fun getAccountInfo(): String = "Account | $owner | Balance: $balance"
}

class SavingsAccount(owner: String, balance: Double) : Account(owner, balance) {
    private val withdrawalFee = 0.10  // 10%

    override fun getAccountInfo(): String {
        val afterFee = balance - (balance * withdrawalFee)
        return "Savings | $owner | Balance: $afterFee"
    }
}

class BusinessAccount(owner: String, balance: Double) : Account(owner, balance) {
    private val flatFee = 3000.0

    override fun getAccountInfo(): String {
        val afterFee = balance - flatFee
        return "Business | $owner | Balance: $afterFee"
    }
}


// ── VISIBILITY MODIFIERS ─────────────────────────
// public    → accessible everywhere (default)
// private   → only inside this class
// protected → this class and subclasses only
// internal  → anywhere within the same module (same app or library)


// ── EXTENSION FUNCTIONS ──────────────────────────
// add a function to an existing class without modifying it
// the class you're extending is called the receiver
// 'this' inside the function refers to the object it's called on
//
// used everywhere in Android — you'll see things like
// String.isValidEmail(), View.hide(), Context.toast() in real codebases

fun String.isValidEmail(): Boolean {
    return this.contains("@") && this.contains(".")
}

fun String.capitalizeWords(): String {
    return this.split(" ").joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
}

fun Double.toNaira(): String = "₦$this"

// calling extension functions — looks like they're built into the class
// "dan@mail.com".isValidEmail()   // true
// "hello world".capitalizeWords() // Hello World
// 1500.0.toNaira()                // ₦1500.0


// ── LAMBDA FUNCTIONS ─────────────────────────────
// a function with no name, written inline and assigned to a variable or passed directly
// syntax: val name: (InputType) -> ReturnType = { parameter -> body }

// lambda assigned to a variable
val double: (Int) -> Int = { number -> number * 2 }
// double(5)  // 10

// when there's only one parameter, you can drop it and use 'it' instead
val triple: (Int) -> Int = { it * 3 }
// triple(5)  // 15

// lambda with multiple parameters
val add: (Int, Int) -> Int = { a, b -> a + b }
// add(3, 4)  // 7

// lambda that returns nothing
val log: (String) -> Unit = { message -> println("LOG: $message") }
// log("User logged in")  // LOG: User logged in

// passing a lambda to a function — the { } block you pass IS the lambda
// this is how Android handles button clicks, Compose UI, and callbacks
// fun performRequest(onSuccess: () -> Unit)  — () -> Unit is a lambda type
// performRequest { println("Done") }         — { println("Done") } is the lambda

// higher order functions — functions that take or return lambdas
// filter, map, and forEach are built-in higher order functions you'll use constantly

val users = listOf("daniel", "saleh", "jamila", "dan")

// filter — keep only items that match the condition, returns a new list
val longNames = users.filter { it.length > 4 }
// println(longNames)  // [daniel, saleh, jamila]

// map — transform every item, returns a new list
val upperNames = users.map { it.uppercase() }
// println(upperNames)  // [DANIEL, SALEH, JAMILA, DAN]

// chaining — filter then map
val result = users
    .filter { it.length > 3 }
    .map { it.replaceFirstChar { c -> c.uppercase() } }
// println(result)  // [Daniel, Saleh, Jamila]

// real backend use — transform a list of DB results into response objects
data class UserEntity(val id: Int, val email: String, val passwordHash: String)
data class UserResponse(val id: Int, val email: String)  // never expose password

val dbResults = listOf(
    UserEntity(1, "dan@mail.com", "hashed123"),
    UserEntity(2, "saleh@mail.com", "hashed456")
)

val apiResponse = dbResults.map { UserResponse(id = it.id, email = it.email) }
// println(apiResponse)  // [UserResponse(id=1, email=dan@mail.com), ...]


// ── SEALED INTERFACE ─────────────────────────────
// like a sealed class but more flexible — a class can implement multiple sealed interfaces
// a sealed class can only extend one parent
//
// use sealed interface when the types need to implement other interfaces too
// common pattern in Android for UI state and Ktor/Spring Boot for result types

sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val message: String, val code: Int = 500) : Result<Nothing>
    object Loading : Result<Nothing>
}

// usage — same pattern as sealed class with 'when'
fun handleResult(result: Result<String>) {
    when (result) {
        is Result.Success -> println("Data: ${result.data}")
        is Result.Error   -> println("Error ${result.code}: ${result.message}")
        is Result.Loading -> println("Loading...")
    }
}

// handleResult(Result.Success("User fetched"))       // Data: User fetched
// handleResult(Result.Error("Not found", code = 404)) // Error 404: Not found
// handleResult(Result.Loading)                        // Loading...

// sealed interface vs sealed class
// sealed class  → extend one parent only, simpler, use most of the time
// sealed interface → implement multiple interfaces, more flexible, use when you need it