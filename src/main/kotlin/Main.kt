// ─────────────────────────────────────────────────────────────────────────────
// KOTLIN NOTES — Android Dev & Backend Dev (Ktor / Spring Boot)
// ─────────────────────────────────────────────────────────────────────────────
// This file is meant to be read, understood, and typed out from scratch.
// Every concept is explained the way it actually appears in production code —
// not toy examples, but the real situations you will face when building apps.
// ─────────────────────────────────────────────────────────────────────────────

//const val BASE_URL = "https://api.myapp.com"
// const is like val but evaluated at compile time, not runtime
// must be defined at the top level (outside any function or class)
// use for truly fixed values: API base URLs, timeout durations, config keys

import kotlin.math.*

fun main() {

    // ─────────────────────────────────────────────
    // VARIABLES — var vs val
    // ─────────────────────────────────────────────
    // Every piece of data in your app lives in a variable.
    // Kotlin gives you two kinds:
    //
    // val — immutable. Once assigned, it cannot be reassigned.
    //       This is the default you should reach for. If a value
    //       won't change, make it val. The compiler will stop you
    //       from accidentally overwriting it later.
    //
    // var — mutable. Can be reassigned as many times as you want.
    //       Use this only when the value genuinely needs to change
    //       over time — a counter, a running total, a flag that flips.
    //
    // In real Android and backend code, val is far more common than var.
    // Immutability makes your code safer and easier to reason about.

    val userId = 1              // will never change — val
    val userEmail = "dan@mail.com"
    var retryCount = 0          // will be incremented — var
    var isSessionActive = true  // will be flipped — var

    retryCount++
    isSessionActive = false

    // userId = 2  // ❌ compile error — val cannot be reassigned


    // ─────────────────────────────────────────────
    // DATA TYPES
    // ─────────────────────────────────────────────
    // Kotlin is statically typed — every variable has a type the compiler
    // knows at compile time. This catches entire categories of bugs before
    // your app ever runs.
    //
    // You rarely write the type explicitly because of type inference —
    // Kotlin figures it out from the value you assign (see next section).
    // But knowing the types matters when you read function signatures,
    // work with APIs, or interact with databases.

    val userId2: Int = 1             // whole numbers — IDs, counts, page numbers
    val fileSize: Long = 104857600L  // larger whole numbers — file sizes, timestamps
    // L suffix marks it as Long
    val price: Double = 29.99        // decimal numbers — prices, coordinates, rates
    val discount: Float = 0.15F      // less precise decimal — used in graphics/animations
    // F suffix marks it as Float
    val isVerified: Boolean = false  // true or false — flags, permissions, toggles
    val username: String = "daniel"  // text — names, emails, tokens, messages
    val initial: Char = 'D'          // single character — rarely used directly

    // Any — can hold any type, but avoid it in real code
    // once you use Any, the compiler can't help you — you lose all type safety
    // you'll sometimes see it in legacy code or very generic utilities
    var anything: Any = "a string"
    anything = 42       // allowed, but now you've lost the type information
    anything = true     // still allowed — but this is a code smell


    // ─────────────────────────────────────────────
    // TYPE INFERENCE
    // ─────────────────────────────────────────────
    // Kotlin can figure out the type from the value you assign.
    // You don't need to write the type explicitly in most cases.
    // The compiler knows — and it still enforces the type strictly.
    // This is the form you'll write 99% of the time.

    val email = "dan@mail.com"    // inferred as String
    val age = 25                  // inferred as Int
    val taxRate = 0.075           // inferred as Double
    val isPremiumUser = true      // inferred as Boolean

    // the type is still fixed — you can't reassign a different type
    // var count = 0
    // count = "hello"  // ❌ compile error — count is an Int, not a String


    // ─────────────────────────────────────────────
    // NULL SAFETY
    // ─────────────────────────────────────────────
    // null means "no value" — the data doesn't exist, hasn't arrived yet,
    // or couldn't be found. This is one of the most common sources of crashes
    // in mobile and backend apps.
    //
    // In Java, any variable can be null at any time and the compiler
    // says nothing. You only find out at runtime when the app crashes
    // with a NullPointerException (NPE). This is so common it's been
    // called "the billion dollar mistake."
    //
    // Kotlin fixes this at the type system level:
    //   String  → guaranteed to have a value. Compiler won't let you assign null.
    //   String? → explicitly nullable. Compiler forces you to handle the null case.
    //
    // The ? doesn't come from you writing = null yourself.
    // It comes from real situations where data might genuinely not exist:
    //
    //   val user = userRepository.findById(id)
    //   → returns User? because the user might not exist in the database
    //
    //   val token = request.headers["Authorization"]
    //   → returns String? because the client might not send this header
    //
    //   val input = readLine()
    //   → returns String? because the user might press enter with nothing typed
    //
    //   val age = "abc".toIntOrNull()
    //   → returns Int? because "abc" cannot be converted to a number
    //
    // The ? in the type comes from the function's return type declaration.
    // You just receive the value and handle it safely.

    // Simulating a database lookup — returns the email if found, null if not
    fun findUserEmail(id: Int): String? {
        return if (id == 1) "dan@mail.com" else null
    }

    val foundEmail = findUserEmail(1)    // String? — user 1 exists
    val missingEmail = findUserEmail(99) // String? — user 99 does not exist


    // ── ?. SAFE CALL ─────────────────────────────
    // Before null safety, you'd write:
    //   if (email != null) { email.uppercase() }
    //
    // The safe call operator does this inline on a single line.
    // If the value is null, it skips the method call entirely
    // and returns null instead of crashing.
    // If the value exists, it calls the method normally.

    println(foundEmail?.uppercase())   // DAN@MAIL.COM — email exists, method runs
    println(missingEmail?.uppercase()) // null — email is null, method is skipped

    // You can chain safe calls — each step is skipped if any part is null
    // In Android you'll see this with nested objects:
    // val city = user?.address?.city  — safe at every level


    // ── ?: ELVIS OPERATOR ────────────────────────
    // Returning or displaying null is rarely what you want.
    // The Elvis operator gives you a fallback value when the left side is null.
    // Read it as: "use this value, OR IF null, use that instead."
    // Named after Elvis Presley — the ?: symbol looks like his hair when rotated.

    val displayEmail = foundEmail ?: "No email on file"
    val displayMissing = missingEmail ?: "No email on file"
    println(displayEmail)    // dan@mail.com — foundEmail had a value
    println(displayMissing)  // No email on file — missingEmail was null

    // Real backend use: return a safe default when a DB field is missing
    fun getDisplayName(id: Int): String {
        return findUserEmail(id) ?: "Guest"
    }
    println(getDisplayName(1))   // dan@mail.com
    println(getDisplayName(99))  // Guest

    // Elvis can also throw an exception — useful in backend validation
    // val token = request.headers["Authorization"] ?: throw UnauthorizedException()


    // ── ?.let — RUN A BLOCK ONLY IF NOT NULL ─────
    // Elvis gives you a fallback value.
    // ?.let gives you a fallback block — a group of statements that only
    // run when the value is not null. Inside the block, 'it' refers to
    // the confirmed non-null value. The compiler knows it's safe here.

    foundEmail?.let {
        // this entire block only runs because foundEmail is not null
        // 'it' is "dan@mail.com" — guaranteed non-null inside
        println(it.uppercase())    // DAN@MAIL.COM
        println(it.length)         // 13
        println(it.split("@")[0])  // dan — extract username from email
    }

    missingEmail?.let {
        println("This never prints — block is skipped because missingEmail is null")
    }

    // Real Android use — only update the UI if the data actually came back
    // user?.let {
    //     nameTextView.text = it.name
    //     emailTextView.text = it.email
    //     avatarImageView.load(it.profilePictureUrl)
    // }
    //
    // Real backend use — only send a notification if the token exists
    // deviceToken?.let { pushNotificationService.send(it, message) }


    // ── NULLABLE BOOLEAN ─────────────────────────
    // Boolean  → 2 states: true, false
    // Boolean? → 3 states: true, false, null
    //
    // This comes up when a flag in your database hasn't been set yet.
    // For example, isEmailVerified might be null for a user who just
    // registered — it's not false (they didn't fail verification),
    // it's unknown (they haven't been through the process yet).
    //
    // You can't use if (isEmailVerified) directly because the compiler
    // won't accept Boolean? where it expects Boolean.
    // You must be explicit about how you handle the null case.

    val isEmailVerified: Boolean? = null  // just registered, not yet verified

    // == true: only passes for actual true, null and false both go to else
    if (isEmailVerified == true) {
        println("Email confirmed — grant full access")
    } else {
        println("Email not confirmed or unknown — restrict access")  // prints this
    }

    // Alternative with Elvis — collapse null into a default boolean first
    // isEmailVerified ?: false means: use isEmailVerified if not null, else use false
    if (isEmailVerified ?: false) {
        println("Email confirmed")
    } else {
        println("Email not confirmed or unknown")  // prints this
    }


    // ─────────────────────────────────────────────
    // STRINGS
    // ─────────────────────────────────────────────
    // Strings are text. In Android and backend work you'll deal with
    // strings constantly — user input, API responses, database values,
    // error messages, tokens, URLs. Knowing how to manipulate them
    // cleanly is essential.

    // .trim() — removes leading and trailing whitespace
    // Always trim user input before validating or saving it.
    // Users accidentally add spaces when typing in form fields.
    val rawInput = "   user@gmail.com   "
    println(rawInput.trim())       // user@gmail.com
    println(rawInput.trimStart())  // "user@gmail.com   " — left side only
    println(rawInput.trimEnd())    // "   user@gmail.com" — right side only

    // .length — number of characters
    // Use for input validation: minimum password length, max bio characters
    val password = "pass"
    if (password.length < 8) println("Password must be at least 8 characters")

    // .uppercase() / .lowercase()
    // Returns a NEW string — the original is unchanged.
    // Use for display formatting and for normalization before comparison.
    val inputRole = "ADMIN"
    println(inputRole.lowercase())  // admin
    println(inputRole.uppercase())  // ADMIN — unchanged, was already upper

    // Case-insensitive comparison — normalize both sides before comparing
    // so "ADMIN", "admin", and "Admin" all match the same stored value
    val storedRole = "admin"
    if (inputRole.equals(storedRole, ignoreCase = true)) {
        println("Role matches — grant admin permissions")
    }

    // .contains() — check if a substring exists inside the string
    // Case-sensitive by default — use ignoreCase for user-facing search
    val postContent = "How to build Android apps with Kotlin"
    println(postContent.contains("Android"))                     // true
    println(postContent.contains("android"))                     // false — case matters
    println(postContent.contains("android", ignoreCase = true))  // true

    // .replace() — swap out part of a string, returns a new string
    // Real use: sanitize data before saving, format phone numbers
    val rawPhone = "+234-801-000-0000"
    val cleanPhone = rawPhone.replace("-", "")  // store without dashes
    println(cleanPhone)  // +2348010000000

    // .split() — break a string into a List<String> at every delimiter
    // Real use: parse comma-separated values from an API, split query params
    val csvRow = "daniel,25,Lagos,admin"
    val columns = csvRow.split(",")
    println(columns[0])  // daniel
    println(columns[2])  // Lagos

    // .substring(startIndex, endIndex) — extract a portion of the string
    // startIndex is inclusive, endIndex is exclusive
    // Real use: parse structured strings, extract parts of tokens or dates
    val isoDate = "2025-06-13"
    val year  = isoDate.substring(0, 4)   // 2025
    val month = isoDate.substring(5, 7)   // 06
    val day   = isoDate.substring(8, 10)  // 13
    println("Year: $year, Month: $month, Day: $day")

    // .startsWith() / .endsWith()
    // Real use: validate that a URL is secure, check file extensions before upload
    val apiUrl = "https://api.myapp.com/v1/users"
    if (!apiUrl.startsWith("https")) println("Insecure URL — reject this")

    val uploadedFile = "profile_picture.jpg"
    val allowedExtensions = listOf(".jpg", ".jpeg", ".png", ".webp")
    val isAllowed = allowedExtensions.any { uploadedFile.endsWith(it) }
    println(isAllowed)  // true

    // .isEmpty() vs .isBlank()
    // isEmpty() → true only if the string has zero characters
    // isBlank()  → true if empty OR contains only whitespace
    // Always use isBlank() when validating form input — a string of spaces
    // passes isEmpty() but is still useless and should be rejected
    val emptyField = ""
    val whitespaceField = "     "
    println(emptyField.isEmpty())       // true
    println(whitespaceField.isEmpty())  // false — it has characters (spaces)
    println(whitespaceField.isBlank())  // true  — but it's all whitespace

    // Real validation function using what you've learned
    fun validateEmail(input: String): String {
        val trimmed = input.trim()
        if (trimmed.isBlank()) return "Email cannot be empty"
        if (!trimmed.contains("@")) return "Invalid email format"
        return trimmed  // return the clean version
    }
    println(validateEmail("  "))               // Email cannot be empty
    println(validateEmail("notanemail"))        // Invalid email format
    println(validateEmail("  dan@mail.com  "))  // dan@mail.com

    // String templates — embed variables and expressions directly in strings
    // Far cleaner than concatenating with +
    val displayName = "Daniel"
    val loginCount = 42
    println("Welcome back, $displayName!")
    println("You have logged in $loginCount times.")
    println("Your email domain is ${displayName.lowercase()}@app.com")
    // Use $variableName for simple values
    // Use ${expression} for any Kotlin expression

    // String comparison — always use == in Kotlin
    // == compares the value (content), not the memory location
    // This is different from Java where == compares references
    val tokenA = "eyJhbGciOiJIUzI1NiJ9"
    val tokenB = "eyJhbGciOiJIUzI1NiJ9"
    println(tokenA == tokenB)  // true — same content, this is what you want


    // ─────────────────────────────────────────────
    // ARITHMETIC OPERATORS
    // ─────────────────────────────────────────────

    val itemPrice = 5000.0
    val itemCount = 3
    val taxRate2 = 0.075

    println(itemPrice * itemCount)              // 15000.0 — subtotal
    println(itemPrice * itemCount * taxRate2)   // 1125.0  — tax amount
    println(itemPrice * itemCount * (1 + taxRate2)) // 16125.0 — total with tax

    // Integer division drops the decimal — be careful with this
    println(10 / 3)    // 3, not 3.33 — both sides are Int, result is Int
    println(10.0 / 3)  // 3.3333... — one side is Double, result is Double

    // % modulus — the remainder after division
    // Real use: check even/odd, paginate data, wrap around a circular list
    println(10 % 2)  // 0 — even number
    println(11 % 2)  // 1 — odd number
    println(7 % 3)   // 1 — 7 divided by 3 is 2 remainder 1


    // ─────────────────────────────────────────────
    // KOTLIN.MATH
    // ─────────────────────────────────────────────
    // import kotlin.math.* at the top gives you access to all math functions

    // max / min — useful when enforcing limits
    val requestedLimit = 200
    val maxAllowed = 100
    val actualLimit = min(requestedLimit, maxAllowed)  // never exceed 100
    println(actualLimit)  // 100

    // abs — absolute value, removes the negative sign
    // Real use: calculate distance between two values regardless of direction
    val temperatureDiff = abs(-12.5)
    println(temperatureDiff)  // 12.5

    println(2.0.pow(10))   // 1024.0 — 2 to the power of 10
    println(sqrt(256.0))   // 16.0   — square root of 256
    println(round(4.567))  // 5.0    — nearest whole number
    println(floor(4.9))    // 4.0    — always rounds down
    println(ceil(4.1))     // 5.0    — always rounds up


    // ─────────────────────────────────────────────
    // INCREMENT AND DECREMENT
    // ─────────────────────────────────────────────
    // ++ adds 1, -- subtracts 1
    // The position of the operator matters:
    //   postfix (count++) — uses the current value FIRST, then changes it
    //   prefix  (++count) — changes the value FIRST, then uses the new value

    var attemptCount = 0
    println(attemptCount++)  // 0 — prints 0, then becomes 1
    println(attemptCount)    // 1 — now it's 1
    println(++attemptCount)  // 2 — becomes 2 first, then prints 2

    // In practice, most real code just uses count++ or count-- in a loop
    // and reads the variable on a separate line — the prefix/postfix
    // difference only matters when you use the expression inline like above

    // + on strings means concatenation, not addition
    // This is a common source of confusion coming from other languages
    println("Page " + 1)       // Page 1 — number converted to string and joined
    println(1 + 2)              // 3 — both are numbers, this is addition
    // Prefer string templates over + concatenation — cleaner and less error-prone
    val pageNumber = 3
    println("Showing page $pageNumber of 10")  // Showing page 3 of 10


    // ─────────────────────────────────────────────
    // TYPE CONVERSION
    // ─────────────────────────────────────────────
    // Kotlin never converts types automatically — you must do it explicitly.
    // This prevents the silent bugs you get in JavaScript where "5" + 1 = "51".
    //
    // In Android: text from an EditText (form field) is always a String.
    //             If the user types their age or price, you must convert it.
    // In backend: query parameters from a URL come in as Strings.
    //             /users?page=2&limit=50 — "2" and "50" must be converted to Int.

    val pageParam = "2"      // came in as a URL query parameter
    val limitParam = "50"

    val page = pageParam.toInt()    // 2
    val limit = limitParam.toInt()  // 50
    println("Fetching page $page with $limit items")

    println(42.toString())    // "42"  — Int to String
    println(3.99.toInt())     // 3     — Double to Int, drops decimal (does not round)
    println(3.toDouble())     // 3.0   — Int to Double
    println("3.14".toDouble()) // 3.14 — String to Double

    // toIntOrNull / toDoubleOrNull — safe conversion that won't crash
    // Use these whenever the input might not be a valid number.
    // Returns null on failure instead of throwing an exception.
    val userAgeInput = "twenty"  // user typed text instead of a number
    val parsedAge = userAgeInput.toIntOrNull()
    if (parsedAge == null) {
        println("Invalid age input — please enter a number")
    } else {
        println("Age: $parsedAge")
    }

    // Combined with Elvis for a clean one-liner default
    val safeAge = userAgeInput.toIntOrNull() ?: 0
    println(safeAge)  // 0 — fallback when conversion fails


    // ─────────────────────────────────────────────
    // LOGICAL OPERATORS
    // ─────────────────────────────────────────────
    // && — AND: every condition must be true for the result to be true
    // || — OR:  at least one condition must be true
    // !  — NOT: flips the boolean value

    val isAccountActive = true
    val hasValidSubscription = true
    val isEmailConfirmed = false

    // Real use: access control — multiple conditions must pass
    if (isAccountActive && hasValidSubscription && isEmailConfirmed) {
        println("Full access granted")
    } else {
        println("Access restricted — check account status")  // prints this
    }

    // Real use: show a banner if the user has any unresolved issue
    if (!isEmailConfirmed || !hasValidSubscription) {
        println("Show warning banner to user")  // prints this
    }

    // Short-circuit evaluation — Kotlin stops evaluating as soon as the result is known
    // In &&, if the first condition is false, the rest are never checked
    // In ||, if the first condition is true, the rest are never checked
    // This matters when the second condition involves a function call or DB query


    // ─────────────────────────────────────────────
    // COMPARISON OPERATORS
    // ─────────────────────────────────────────────

    val requestedPage = 5
    val totalPages = 10

    println(requestedPage > totalPages)   // false
    println(requestedPage < totalPages)   // true
    println(requestedPage >= 1)           // true  — valid page
    println(requestedPage <= totalPages)  // true  — within range
    println(requestedPage == 5)           // true  — exact match
    println(requestedPage != 1)           // true  — not the first page

    // Real use: validate that a page number is within bounds
    if (requestedPage in 1..totalPages) {
        println("Valid page — fetch data")
    } else {
        println("Page out of range — return 400 Bad Request")
    }


    // ─────────────────────────────────────────────
    // IF / ELSE IF / ELSE
    // ─────────────────────────────────────────────

    val httpStatusCode = 404

    if (httpStatusCode == 200) {
        println("Success")
    } else if (httpStatusCode == 201) {
        println("Created")
    } else if (httpStatusCode == 400) {
        println("Bad Request — check your input")
    } else if (httpStatusCode == 401) {
        println("Unauthorized — login required")
    } else if (httpStatusCode == 404) {
        println("Not Found")  // prints this
    } else if (httpStatusCode == 500) {
        println("Server Error — something went wrong on our side")
    } else {
        println("Unexpected status code")
    }

    // if as an expression — in Kotlin, if returns a value
    // This replaces the ternary operator (condition ? a : b) from Java
    val statusLabel = if (httpStatusCode in 200..299) "Success" else "Failed"
    println(statusLabel)  // Failed

    // Real Android use — set button text based on state
    // val buttonText = if (isLoading) "Loading..." else "Submit"
    // submitButton.text = buttonText


    // ─────────────────────────────────────────────
    // WHEN EXPRESSION
    // ─────────────────────────────────────────────
    // when is the cleaner, more powerful version of switch/case.
    // Unlike Java's switch, it returns a value — making it an expression.
    // The compiler can warn you if you haven't covered all cases (when used
    // with sealed classes or enums, it's exhaustive).

    // when as a statement — just executing code per case
    when (httpStatusCode) {
        200       -> println("OK")
        201       -> println("Created")
        400       -> println("Bad Request")
        401, 403  -> println("Auth error")  // multiple values in one branch
        in 404..499 -> println("Client error: $httpStatusCode")  // range
        500       -> println("Internal Server Error")
        else      -> println("Unhandled status: $httpStatusCode")
    }

    // when as an expression — returns a value, assign it to a variable
    val errorCategory = when (httpStatusCode) {
        in 200..299 -> "Success"
        in 400..499 -> "Client Error"
        in 500..599 -> "Server Error"
        else        -> "Unknown"
    }
    println(errorCategory)  // Client Error

    // when with ranges — great for age groups, score tiers, pricing bands
    val orderValue = 75000.0
    val shippingCost = when {
        orderValue >= 100000 -> 0.0       // free shipping
        orderValue >= 50000  -> 1500.0    // reduced shipping
        else                 -> 3000.0    // standard shipping
    }
    println("Shipping: ₦$shippingCost")  // Shipping: ₦1500.0


    // ─────────────────────────────────────────────
    // ARRAYS
    // ─────────────────────────────────────────────
    // An array is a fixed-size collection of elements.
    // Once created, you cannot add or remove elements — only update them.
    // In real Android and backend work, you'll use List almost exclusively.
    // Arrays are worth knowing because some APIs return them and some
    // lower-level operations use them, but don't reach for them first.

    val supportedImageTypes = arrayOf("jpg", "jpeg", "png", "webp")
    println(supportedImageTypes[0])                 // jpg — access by index, starts at 0
    println(supportedImageTypes.contentToString())  // [jpg, jpeg, png, webp]
    println(supportedImageTypes.size)               // 4
    println(supportedImageTypes.contains("png"))    // true

    // Typed arrays — more memory-efficient for primitive types
    // Use IntArray instead of Array<Int> when working with lots of numbers
    val statusCodes = intArrayOf(200, 201, 400, 401, 500)

    // Array constructor — generate values with a formula
    // Array(size) { index -> value }
    val pageNumbers = Array(5) { index -> index + 1 }
    println(pageNumbers.contentToString())  // [1, 2, 3, 4, 5]

    // arrayOfNulls — when you know the size but not the values yet
    val uploadSlots = arrayOfNulls<String>(3)
    uploadSlots[0] = "receipt.jpg"
    uploadSlots[1] = "id_front.jpg"
    println(uploadSlots.contentToString())  // [receipt.jpg, id_front.jpg, null]


    // ─────────────────────────────────────────────
    // LISTS
    // ─────────────────────────────────────────────
    // List is the collection you'll use most in real code.
    // Unlike arrays, mutable lists can grow and shrink.
    // Kotlin gives you two versions:
    //
    // listOf() — read-only. You cannot add, remove, or change elements.
    //            Use when the data is fixed after creation:
    //            a list of supported currencies, navigation destinations,
    //            the results of a DB query you're just reading.
    //
    // mutableListOf() — can add, remove, and update freely.
    //                   Use when the list grows over time:
    //                   items in a shopping cart, posts loaded from an API,
    //                   search results being built up.

    // Read-only list
    val allowedRoles: List<String> = listOf("admin", "editor", "viewer")
    println(allowedRoles[0])    // admin
    println(allowedRoles.size)  // 3
    // allowedRoles.add("guest")  // ❌ compile error — read-only

    // Mutable list — building a list of notifications dynamically
    val notifications = mutableListOf<String>()
    notifications.add("New message from Saleh")
    notifications.add("Payment of ₦5,000 received")
    notifications.add(0, "Security alert: new login detected")  // insert at front
    notifications[1] = "Updated: new message from Saleh"        // update by index
    notifications.remove("Payment of ₦5,000 received")          // remove by value
    println(notifications)

    // Common methods — available on both List and MutableList
    val recentSearches = listOf("kotlin", "android", "ktor", "android", "spring boot")
    println(recentSearches.first())                    // kotlin
    println(recentSearches.last())                     // spring boot
    println(recentSearches.size)                       // 5
    println(recentSearches.contains("kotlin"))         // true
    println(recentSearches.indexOf("android"))         // 1 — first occurrence
    println(recentSearches.lastIndexOf("android"))     // 3 — last occurrence
    println(recentSearches.isEmpty())                  // false
    println(recentSearches.distinct())                 // removes duplicates
    println(recentSearches.reversed())                 // reversed order
    println(recentSearches.sorted())                   // alphabetical order

    // Destructuring — unpack list elements into named variables
    // _ skips a position you don't need
    val (firstName, _, city2) = listOf("Daniel", "Okafor", "Abuja")
    println(firstName)  // Daniel
    println(city2)      // Abuja — middle element skipped


    // ─────────────────────────────────────────────
    // FOR LOOP
    // ─────────────────────────────────────────────

    val pendingOrders = listOf("ORD-001", "ORD-002", "ORD-003")

    // Basic loop — do something with each element
    for (order in pendingOrders) {
        println("Processing $order")
    }

    // Loop with index — when you need to know the position
    for ((index, order) in pendingOrders.withIndex()) {
        println("${index + 1}. $order")  // 1. ORD-001, 2. ORD-002, etc.
    }

    // Ranges — loop a specific number of times
    for (i in 1..5) println(i)           // 1 2 3 4 5 — both ends inclusive
    for (i in 1 until 5) println(i)      // 1 2 3 4   — excludes the end
    for (i in 10 downTo 1) println(i)    // 10 9 8 ... 1 — countdown
    for (i in 0 until 100 step 10) println(i) // 0 10 20 30 ... 90 — every 10th

    // Real backend use: send reminders for the next 7 days
    // for (dayOffset in 0 until 7) {
    //     val date = LocalDate.now().plusDays(dayOffset.toLong())
    //     reminderService.scheduleFor(date)
    // }


    // ─────────────────────────────────────────────
    // FOREACH
    // ─────────────────────────────────────────────
    // forEach is cleaner than a for loop when you just need each element.
    // It takes a lambda — the { } block is the code that runs per element.
    // 'it' is the implicit name for the current element.

    pendingOrders.forEach { order ->
        println("Dispatching $order")
    }

    // When the lambda is short, use 'it' instead of naming the parameter
    pendingOrders.forEach { println(it) }

    // forEachIndexed — when you need the index too
    pendingOrders.forEachIndexed { index, order ->
        println("Queue position ${index + 1}: $order")
    }

    // Real backend use: send a push notification to every active device token
    val deviceTokens = listOf("token_abc", "token_def", "token_ghi")
    deviceTokens.forEach { token ->
        println("Sending push notification to $token")
        // pushNotificationService.send(token, "You have a new message")
    }


    // ─────────────────────────────────────────────
    // WHILE LOOP
    // ─────────────────────────────────────────────
    // Use while when you don't know the number of iterations upfront.
    // The loop continues as long as the condition is true.
    // Common in backend: polling for a result, retry logic for network calls.

    var connectionAttempts = 0
    val maxAttempts = 3
    var connected = false

    while (connectionAttempts < maxAttempts && !connected) {
        connectionAttempts++
        println("Connection attempt $connectionAttempts...")
        connected = connectionAttempts == 3  // simulating success on 3rd try
    }

    if (connected) println("Connected successfully")
    else println("Failed after $maxAttempts attempts — check your network")


    // ─────────────────────────────────────────────
    // DO WHILE
    // ─────────────────────────────────────────────
    // Like while, but the block runs at least once before the condition is checked.
    // Use when you always need one execution before deciding whether to continue.

    var pageToFetch = 1
    var hasMoreData = true

    do {
        println("Fetching page $pageToFetch...")
        // In real code: val data = api.getPage(pageToFetch)
        //               hasMoreData = data.isNotEmpty()
        //               results.addAll(data)
        pageToFetch++
        hasMoreData = pageToFetch <= 3  // simulate 3 pages of data
    } while (hasMoreData)


    // ─────────────────────────────────────────────
    // BREAK AND CONTINUE
    // ─────────────────────────────────────────────

    // break — exit the loop entirely when you've found what you need
    // No point processing 10,000 records if you found the match at record 3
    val transactionIds = listOf("TXN-100", "TXN-101", "TXN-102", "TXN-103", "TXN-104")
    val fraudulentId = "TXN-102"

    for (txnId in transactionIds) {
        if (txnId == fraudulentId) {
            println("Fraudulent transaction found: $txnId — stopping processing")
            break
        }
        println("Processed $txnId — OK")
    }

    // continue — skip the current element and move to the next
    // Real use: skip invalid or incomplete records in a data processing job
    val rawAmounts = listOf(5000.0, -200.0, 15000.0, 0.0, 8500.0)

    for (amount in rawAmounts) {
        if (amount <= 0) {
            println("Skipping invalid amount: $amount")
            continue
        }
        println("Processing transaction of ₦$amount")
    }


    // ─────────────────────────────────────────────
    // CALLING FUNCTIONS AND CLASSES
    // ─────────────────────────────────────────────

    connectToDb()
    println(fetchUserById(id = 5))
    createUser(email = "saleh@mail.com", username = "saleh", role = "editor")
    println(fetchPaginatedPosts())
    println(fetchPaginatedPosts(page = 2, limit = 50))
    println(computeOrderTotal(price = 25000.0, taxRate = 0.075))
    println(checkTokenValidity(token = null))      // false
    println(checkTokenValidity(token = "tok_123")) // true
    println(authenticateUser(email = "", password = "abc"))         // Email required
    println(authenticateUser(email = "a@b.com", password = "abc")) // Password too short
    println(authenticateUser(email = "a@b.com", password = "secure123")) // Authenticated
    sendHttpRequest(
        onSuccess = { println("Response received — update the UI") },
        onError   = { println("Request failed — show error message") }
    )
    println(formatToNaira(amount = 45000.0))  // ₦45000.0

    // Classes
    val newUser = User2(id = 1, email = "dan@mail.com", password = "securePass1")
    println(newUser)                // User(id=1, email=dan@mail.com, role=viewer)
    println(newUser.displayEmail)   // [1] dan@mail.com — custom getter
    newUser.trustScore = -50        // custom setter blocks negatives
    println(newUser.trustScore)     // 0 — setter rejected -50
    newUser.trustScore = 85
    println(newUser.trustScore)     // 85
    newUser.suspend()
    println(newUser.isActive)       // false

    val adminUser = User2.createAdmin(email = "admin@myapp.com", password = "adminPass1")
    println(adminUser.role)  // admin

    // Data class
    val loginRequest = LoginRequest(email = "dan@mail.com", password = "securePass1")
    val loginRequest2 = LoginRequest(email = "dan@mail.com", password = "securePass1")
    println(loginRequest)            // LoginRequest(email=dan@mail.com, password=securePass1)
    println(loginRequest == loginRequest2)  // true — data class compares by value

    val updatedRequest = loginRequest.copy(email = "new@mail.com")
    println(updatedRequest.email)    // new@mail.com
    println(loginRequest.email)      // dan@mail.com — original unchanged

    // Enum
    val currentRole = AppRole.ADMIN
    val accessLevel = when (currentRole) {
        AppRole.ADMIN  -> "Full system access"
        AppRole.EDITOR -> "Content management access"
        AppRole.VIEWER -> "Read-only access"
    }
    println("${currentRole.displayName}: $accessLevel")

    // Sealed class
    val result = fetchUserProfile(userId = 1)
    when (result) {
        is NetworkResult.Success -> println("Profile loaded: ${result.data}")
        is NetworkResult.Error   -> println("Error ${result.code}: ${result.message}")
        is NetworkResult.Loading -> println("Show loading state")
    }

    // Singleton
    AppDatabase.initialize()
    println(AppDatabase.connectionUrl)

    // Interface — polymorphism
    val services: List<NotificationService2> = listOf(
        EmailService(),
        SmsService(),
        PushNotificationService()
    )
    // Same function call on each — each behaves differently
    // This is polymorphism: one type (NotificationService2), many forms
    services.forEach { service ->
        service.send(to = "dan@mail.com", payload = "Your order has shipped")
    }

    // Abstract class
    val userRepo = UserRepo()
    userRepo.saveRecord("dan@mail.com")
    userRepo.deleteRecord("dan@mail.com")

    // Inheritance and polymorphism with accounts
    val bankAccounts: List<BankAccount> = listOf(
        SavingsAccount2(owner = "Daniel", balance = 100000.0),
        BusinessAccount2(owner = "Saleh Corp", balance = 5000000.0),
        LoanAccount(owner = "James", balance = 250000.0, interestRate = 0.18)
    )
    bankAccounts.forEach { println(it.getSummary()) }

    // Extension functions
    println("dan@mail.com".isValidEmail())            // true
    println("notanemail".isValidEmail())              // false
    println("activate user account".toTitleCase())    // Activate User Account
    println(49999.99.toNaira())                        // ₦49999.99

    // Lambda and higher-order functions
    val rawUsers = listOf(
        UserRecord(id = 1, email = "dan@mail.com", passwordHash = "h1", isActive = true),
        UserRecord(id = 2, email = "saleh@mail.com", passwordHash = "h2", isActive = false),
        UserRecord(id = 3, email = "james@mail.com", passwordHash = "h3", isActive = true)
    )

    // Filter to only active users, then map to safe response objects (no password hash)
    val activeUserResponses = rawUsers
        .filter { it.isActive }
        .map { SafeUserResponse(id = it.id, email = it.email) }
    println(activeUserResponses)

    // Sealed interface result wrapper
    val profileResult = loadUserProfile(userId = 1)
    when (profileResult) {
        is OperationResult.Success -> println("Data: ${profileResult.data}")
        is OperationResult.Error   -> println("Error ${profileResult.code}: ${profileResult.message}")
        is OperationResult.Loading -> println("Loading...")
    }

}


// ─────────────────────────────────────────────────────────────────────────────
// FUNCTIONS
// ─────────────────────────────────────────────────────────────────────────────
// Functions are defined outside main (or inside classes).
// They are public by default — no need to write 'public'.
// Return type is Unit by default — Unit means nothing is returned,
// equivalent to void in Java. You don't need to write ': Unit' explicitly.

fun connectToDb() {
    println("Connecting to PostgreSQL database...")
    // In real code: DataSource.getConnection()
}

// Declaring a return type after the parentheses
// Single expression function — when the body is one expression,
// drop the curly braces and the return keyword
fun fetchUserById(id: Int): String = "SELECT * FROM users WHERE id = $id"

// Named arguments — the caller can pass arguments in any order
// and name them explicitly. Makes call sites self-documenting.
// Real use: when a function has many parameters, naming them at the
// call site makes the code far easier to read and understand.
fun createUser(email: String, username: String, role: String) {
    println("INSERT INTO users (email, username, role) VALUES ('$email', '$username', '$role')")
}

// Default arguments — a parameter gets a fallback value if the caller
// doesn't provide one. Common for paginated API endpoints where page=1
// and limit=20 are sensible defaults that callers can override.
fun fetchPaginatedPosts(page: Int = 1, limit: Int = 20): String {
    return "Fetching $limit posts from page $page"
}

fun computeOrderTotal(price: Double, taxRate: Double): Double = price + (price * taxRate)

// isLoggedIn is a single expression function — returns Boolean
fun checkTokenValidity(token: String?) = token != null

// Return as early exit — validate inputs at the top, return immediately
// if anything is wrong. The happy path (success case) stays at the bottom
// of the function. This pattern is called "guard clauses."
fun authenticateUser(email: String, password: String): String {
    if (email.isBlank()) return "Email is required"
    if (!email.contains("@")) return "Invalid email format"
    if (password.length < 6) return "Password too short — minimum 6 characters"
    // Everything passed — proceed with authentication
    return "User authenticated successfully"
}

// Function as argument — accepts a lambda as a parameter.
// This is how you pass behaviour into a function, not just data.
// () -> Unit means: a function that takes no arguments and returns nothing.
// Real use in Android: button click handlers, Jetpack Compose event callbacks
// Real use in Ktor/Spring Boot: middleware, response handlers, async callbacks
fun sendHttpRequest(onSuccess: () -> Unit, onError: () -> Unit) {
    val requestSucceeded = true  // simulate
    if (requestSucceeded) onSuccess() else onError()
}

// Lambda stored in a variable
// (Double) -> String means: takes a Double, returns a String
val formatToNaira: (Double) -> String = { amount -> "₦$amount" }


// ─────────────────────────────────────────────────────────────────────────────
// CLASSES
// ─────────────────────────────────────────────────────────────────────────────
// A class is a blueprint. An object is the actual thing created from that blueprint.
//
// Every user in your app is an object created from the User class.
// Every post, every order, every notification — all objects from their class blueprints.
// Classes group related data (properties) and behaviour (functions) together.

// PRIMARY CONSTRUCTOR
// Parameters go directly in the class header.
// val/var in the header = property stored on the object, accessible from outside.
// Without val/var = just a constructor parameter, usable in init but not outside.

class User2(
    val id: Int,
    val email: String,
    private val password: String,  // private — only this class can access it
    // never expose passwords outside the class
    val role: String = "viewer",   // default value — most users start as viewers
    var isActive: Boolean = true
) {
    // INIT BLOCK
    // Runs the moment an object is created.
    // Use it to validate data so an invalid object can never exist.
    // If the require() check fails, the object creation throws an exception.
    init {
        require(email.contains("@")) { "Invalid email format: $email" }
        require(password.length >= 8) { "Password must be at least 8 characters" }
    }

    // CUSTOM GETTER
    // A computed property — no value is stored, it's calculated fresh on every access.
    // Real use: combine or format data for display without storing the formatted version.
    val displayEmail: String
        get() = "[$id] $email"

    // CUSTOM SETTER
    // Intercepts the value before it gets stored.
    // 'field' is the keyword for the actual backing storage of the property.
    // Real use: enforce business rules — a trust score can't be negative.
    var trustScore: Int = 0
        set(value) {
            field = if (value >= 0) value else 0
        }

    fun suspend() {
        isActive = false
        println("User $email has been suspended")
    }

    fun hasRole(requiredRole: String): Boolean = role == requiredRole

    // TOSTRING OVERRIDE
    // Controls what you see when you println(user) or log the object.
    // Without this, you'd see something like: User2@5e2de80c (memory address — useless)
    override fun toString() = "User(id=$id, email=$email, role=$role)"

    // COMPANION OBJECT
    // Belongs to the class itself, not to any instance of the class.
    // Call it as User2.createAdmin() — no object creation needed.
    // Similar to static methods in Java.
    // Real use: factory methods that create specific types of the object.
    companion object {
        fun createAdmin(email: String, password: String) =
            User2(id = 0, email = email, password = password, role = "admin")
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// DATA CLASS
// ─────────────────────────────────────────────────────────────────────────────
// Designed purely to hold data — like a row from the database or a JSON body.
// Kotlin automatically generates:
//   toString()  — readable print output
//   equals()    — compares by value, not by memory location
//   hashCode()  — needed for use in Sets and as Map keys
//   copy()      — create a new object with some fields changed
//
// Use data classes for every model in your app:
//   LoginRequest, RegisterRequest (incoming API request bodies)
//   UserResponse, PostResponse (outgoing API response bodies)
//   UserEntity, PostEntity (database row representations)

data class LoginRequest(
    val email: String,
    val password: String
)

data class UserProfileResponse(
    val id: Int,
    val email: String,
    val role: String,
    val isActive: Boolean
)

// copy() creates a modified version — original is unchanged
// Real use: update a record for patching without mutating the original
// val updated = existingUser.copy(isActive = false)


// ─────────────────────────────────────────────────────────────────────────────
// ENUM CLASS
// ─────────────────────────────────────────────────────────────────────────────
// A fixed, named set of constants.
// Safer than scattering raw strings like "admin" throughout your code.
// If you typo "ADMON" the compiler catches it immediately.
// If you typo "admon" as a string, it silently fails at runtime.
//
// Each constant can carry its own properties and functions.
// Real use: user roles, order statuses, HTTP methods, navigation routes,
//           subscription plan types, notification priorities.

enum class AppRole(val displayName: String, val accessLevel: Int) {
    ADMIN("Administrator", 3),
    EDITOR("Editor", 2),
    VIEWER("Viewer", 1);

    fun canEdit() = accessLevel >= 2
    fun canDelete() = accessLevel >= 3
}


// ─────────────────────────────────────────────────────────────────────────────
// SEALED CLASS
// ─────────────────────────────────────────────────────────────────────────────
// A sealed class is a restricted class hierarchy — all subclasses must be
// defined in the same file. The compiler knows every possible subclass.
//
// When you use 'when' on a sealed class, it's exhaustive — the compiler
// will tell you if you forget to handle a case. This is powerful because
// adding a new subclass forces you to handle it everywhere in your codebase.
//
// The most common use is representing the state of a network call:
// it's either loading, it succeeded with data, or it failed with an error.
// This is something you'll write in every single Android ViewModel.

sealed class NetworkResult {
    data class Success(val data: String) : NetworkResult()
    data class Error(val message: String, val code: Int = 500) : NetworkResult()
    object Loading : NetworkResult()
    // object is used here because Loading has no data — there's only one
    // possible "loading" state, so a singleton makes sense
}

fun fetchUserProfile(userId: Int): NetworkResult {
    return if (userId == 1) NetworkResult.Success("Daniel Okafor, Admin")
    else NetworkResult.Error("User not found", code = 404)
}


// ─────────────────────────────────────────────────────────────────────────────
// OBJECT — SINGLETON
// ─────────────────────────────────────────────────────────────────────────────
// The object keyword creates a class that has exactly one instance.
// Kotlin creates it the first time it's accessed and reuses the same
// instance for every subsequent access — this is the Singleton pattern.
//
// Real use: database connection pool (you don't want 50 separate DB connections),
//           HTTP client (one shared client across the app),
//           app-wide configuration, logging service.

object AppDatabase {
    val connectionUrl = "jdbc:postgresql://localhost:5432/myapp_db"
    val maxPoolSize = 10
    private var isInitialized = false

    fun initialize() {
        if (!isInitialized) {
            println("Database connection pool created: $connectionUrl")
            isInitialized = true
        } else {
            println("Database already initialized — reusing existing pool")
        }
    }

    fun getConnection() = println("Borrowing connection from pool")
}


// ─────────────────────────────────────────────────────────────────────────────
// INTERFACE
// ─────────────────────────────────────────────────────────────────────────────
// An interface defines a contract — a set of functions that any implementing
// class must provide. The interface says WHAT to do, not HOW to do it.
//
// Your code talks to the interface, not the specific implementation.
// This means you can swap implementations without changing anything else.
//
// Real use in Android: UserRepository interface — production version talks to
//   the real database, test version uses fake in-memory data.
// Real use in Ktor/Spring Boot: EmailService, SmsService, PushService all
//   implement NotificationService — your endpoint just calls send() and
//   doesn't care which channel is being used.
//
// This is the foundation of dependency injection and testable architecture.

interface NotificationService2 {
    fun send(to: String, payload: String)

    // Interfaces can have default implementations
    // Implementing classes can override this or use the default
    fun isAvailable(): Boolean = true
}

class EmailService : NotificationService2 {
    override fun send(to: String, payload: String) {
        println("EMAIL → $to: $payload")
    }
}

class SmsService : NotificationService2 {
    override fun send(to: String, payload: String) {
        // In real code: twilioClient.sendSms(to, payload)
        println("SMS → $to: $payload")
    }

    override fun isAvailable(): Boolean {
        // In real code: check if SMS credits are available
        return true
    }
}

class PushNotificationService : NotificationService2 {
    override fun send(to: String, payload: String) {
        // In real code: firebaseAdmin.send(deviceToken, payload)
        println("PUSH → $to: $payload")
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// ABSTRACT CLASS
// ─────────────────────────────────────────────────────────────────────────────
// Abstract sits between interface and open class:
//
// Interface        → pure contract, no stored state, no concrete logic
// Abstract class   → can have stored state, can have concrete (shared) logic,
//                    AND can have abstract (must-implement) functions
// Open class       → fully implemented, just open to be extended
//
// Use abstract class when subclasses share real logic that you don't want
// to duplicate, but each subclass must also define its own specific behaviour.
//
// Real use: BaseRepository handles the database connection and error logging
//           (shared), while UserRepository and PostRepository each implement
//           their own specific queries (specific).

abstract class BaseRepo {
    // Concrete function — shared across all subclasses, no duplication needed
    fun saveRecord(data: String) {
        println("Opening transaction...")
        persist(data)
        println("Transaction committed")
    }

    fun deleteRecord(data: String) {
        println("Checking permissions...")
        remove(data)
        println("Audit log written")
    }

    // Abstract functions — subclass MUST implement these
    // They're the specific parts that differ per entity
    abstract fun persist(data: String)
    abstract fun remove(data: String)
}

class UserRepo : BaseRepo() {
    override fun persist(data: String) {
        println("INSERT INTO users: $data")
    }

    override fun remove(data: String) {
        println("DELETE FROM users WHERE email = '$data'")
    }
}

class PostRepo : BaseRepo() {
    override fun persist(data: String) {
        println("INSERT INTO posts: $data")
    }

    override fun remove(data: String) {
        println("DELETE FROM posts WHERE id = '$data'")
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// INHERITANCE AND POLYMORPHISM
// ─────────────────────────────────────────────────────────────────────────────
// Inheritance: a child class gets all the properties and functions of its parent.
// In Kotlin, all classes are final by default — you must mark a class 'open'
// to allow it to be extended.
//
// override: the child class replaces the parent's version of a function
//           with its own implementation.
//
// Polymorphism: one type, many forms.
// You can store different child objects under the parent type and call
// the same function on each — each one responds differently.
//
// This is how Android works:
// - Activity, Fragment, ViewModel are parent classes. Your LoginActivity
//   extends AppCompatActivity — that's inheritance.
// - RecyclerView.ViewHolder holds different types per position — polymorphism.
//
// In Ktor/Spring Boot:
// - Route handlers implement the same interface but each does different work.
// - Exception handlers extend a base class — shared handling, specific messages.

open class BankAccount(
    val owner: String,
    val balance: Double
) {
    open fun getSummary(): String = "Account | $owner | Balance: ₦$balance"

    open fun getMonthlyFee(): Double = 0.0
}

class SavingsAccount2(owner: String, balance: Double) : BankAccount(owner, balance) {
    private val withdrawalFeePercent = 0.05  // 5% fee

    override fun getSummary(): String {
        val balanceAfterFee = balance * (1 - withdrawalFeePercent)
        return "Savings  | $owner | Balance after withdrawal fee: ₦$balanceAfterFee"
    }

    override fun getMonthlyFee() = 500.0
}

class BusinessAccount2(owner: String, balance: Double) : BankAccount(owner, balance) {
    private val flatMonthlyFee = 5000.0

    override fun getSummary(): String {
        val balanceAfterFee = balance - flatMonthlyFee
        return "Business | $owner | Balance after monthly fee: ₦$balanceAfterFee"
    }

    override fun getMonthlyFee() = flatMonthlyFee
}

class LoanAccount(
    owner: String,
    balance: Double,
    val interestRate: Double
) : BankAccount(owner, balance) {
    override fun getSummary(): String {
        val monthlyInterest = balance * (interestRate / 12)
        return "Loan     | $owner | Outstanding: ₦$balance | Monthly interest: ₦$monthlyInterest"
    }
}


// ─────────────────────────────────────────────────────────────────────────────
// VISIBILITY MODIFIERS
// ─────────────────────────────────────────────────────────────────────────────
// Control what parts of your code can access a property or function.
// Good visibility design is what separates clean architecture from a codebase
// where everything touches everything and nothing is safe to change.
//
// public    → accessible from anywhere (this is the default, no keyword needed)
// private   → only inside this class or file. Passwords, internal state.
// protected → this class and any class that extends it. Used with inheritance.
// internal  → anywhere within the same module (same app or library),
//             but not accessible from outside the module. Good for
//             classes that are implementation details of your library.
//
// Rule of thumb: start with private. Make something more visible only when
// you have a specific reason. This prevents accidental coupling between classes.


// ─────────────────────────────────────────────────────────────────────────────
// EXTENSION FUNCTIONS
// ─────────────────────────────────────────────────────────────────────────────
// Add a new function to an existing class without modifying the class itself.
// This is useful for:
//   - Adding helper methods to built-in types (String, Int, List)
//   - Adding utility methods to library classes you don't own
//   - Keeping your code readable by making operations look like they
//     belong to the object rather than being a standalone utility function
//
// 'this' inside the function refers to the object the function was called on.
//
// Real Android examples you'll encounter:
//   String.isValidEmail(), View.show(), View.hide(), Context.showToast()
//   These don't exist in the Android SDK — developers wrote them as extensions.

fun String.isValidEmail(): Boolean {
    val trimmed = this.trim()
    return trimmed.contains("@") && trimmed.contains(".") && trimmed.length > 5
}

fun String.toTitleCase(): String =
    this.split(" ").joinToString(" ") { word ->
        word.replaceFirstChar { it.uppercase() }
    }

// Extension on Double — format currency for Nigerian Naira display
fun Double.toNaira(): String = "₦${String.format("%.2f", this)}"

// Extension on a nullable type — safe to call even when value is null
fun String?.isNullOrBlankSafe(): Boolean = this == null || this.isBlank()

// Extension on List — useful pagination helper
fun <T> List<T>.paginate(page: Int, pageSize: Int): List<T> {
    val fromIndex = (page - 1) * pageSize
    val toIndex = minOf(fromIndex + pageSize, this.size)
    return if (fromIndex >= this.size) emptyList() else this.subList(fromIndex, toIndex)
}


// ─────────────────────────────────────────────────────────────────────────────
// LAMBDA FUNCTIONS
// ─────────────────────────────────────────────────────────────────────────────
// A lambda is a function with no name, written inline.
// You assign it to a variable or pass it directly to another function.
//
// Syntax:    { parameter -> body }
// Type:      (InputType) -> ReturnType
//
// Lambdas are how you pass behaviour into a function — not just data.
// You've already been writing lambdas throughout these notes:
//   .forEach { println(it) }               — { println(it) } is a lambda
//   .filter { it.isActive }                — { it.isActive } is a lambda
//   .map { UserResponse(it.id, it.email) } — also a lambda
//
// HIGHER-ORDER FUNCTIONS
// A higher-order function is a function that takes another function as a
// parameter, or returns a function. filter, map, forEach, and let are all
// higher-order functions built into Kotlin's standard library.

// Lambda stored in a variable — explicit type annotation
val isAdminRole: (String) -> Boolean = { role -> role == "admin" }
println(isAdminRole("admin"))   // true
println(isAdminRole("viewer"))  // false

// When there's only one parameter, you can drop it and use 'it'
val isActiveStatus: (Boolean) -> String = { if (it) "Active" else "Inactive" }
println(isActiveStatus(true))   // Active
println(isActiveStatus(false))  // Inactive

// Lambda with multiple parameters
val buildAuditLog: (String, String) -> String = { action, userId ->
    "ACTION: $action | USER: $userId | TIME: ${System.currentTimeMillis()}"
}
println(buildAuditLog("LOGIN", "user_42"))

// filter — keep only elements where the lambda returns true
// map    — transform every element, returns a new list
// These two together are the most powerful combination in Kotlin collections

data class UserRecord(val id: Int, val email: String, val passwordHash: String, val isActive: Boolean)
data class SafeUserResponse(val id: Int, val email: String)

val dbUsers = listOf(
    UserRecord(1, "dan@mail.com", "hashed_pw_1", isActive = true),
    UserRecord(2, "saleh@mail.com", "hashed_pw_2", isActive = false),
    UserRecord(3, "james@mail.com", "hashed_pw_3", isActive = true),
    UserRecord(4, "amina@mail.com", "hashed_pw_4", isActive = false)
)

// Real backend pattern: filter active users, strip sensitive fields, return clean list
val activeUsersForApi = dbUsers
    .filter { it.isActive }
    .map { SafeUserResponse(id = it.id, email = it.email) }
println(activeUsersForApi)
// [SafeUserResponse(id=1, email=dan@mail.com), SafeUserResponse(id=3, email=james@mail.com)]

// Other useful higher-order functions
val allActive = dbUsers.all { it.isActive }         // false — not all are active
val anyActive = dbUsers.any { it.isActive }          // true  — at least one is
val activeCount = dbUsers.count { it.isActive }      // 2
val firstActive = dbUsers.find { it.isActive }       // first active user or null
val grouped = dbUsers.groupBy { it.isActive }        // Map<Boolean, List<UserRecord>>
println("Active: ${grouped[true]?.size}, Inactive: ${grouped[false]?.size}")


// ─────────────────────────────────────────────────────────────────────────────
// SEALED INTERFACE
// ─────────────────────────────────────────────────────────────────────────────
// Like a sealed class but more flexible:
// A class can implement multiple sealed interfaces.
// A class can only extend ONE parent class (sealed or not).
//
// Use sealed interface when your result types need to implement
// other interfaces too — for example, if Result types also need
// to implement Parcelable in Android, or Serializable for Ktor.
//
// In practice, sealed class handles most cases. Reach for sealed interface
// when you specifically need a type to implement multiple contracts.

sealed interface OperationResult<out T> {
    data class Success<T>(val data: T) : OperationResult<T>
    data class Error(val message: String, val code: Int = 500) : OperationResult<Nothing>
    object Loading : OperationResult<Nothing>
}

fun loadUserProfile(userId: Int): OperationResult<String> {
    return when (userId) {
        1    -> OperationResult.Success("Daniel Okafor — Admin")
        else -> OperationResult.Error("User $userId not found", code = 404)
    }
}

// The generic <out T> means the result can carry any type of data:
// OperationResult<String>, OperationResult<User>, OperationResult<List<Post>>
// The same wrapper works across your entire app