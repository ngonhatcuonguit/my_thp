package com.cuongngo.my_thp.data.database.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cuongngo.my_thp.response.BaseModel
import kotlin.random.Random

@Entity(
    tableName = "users",
    indices = [Index(value = ["personal_number"], unique = true)],
)
data class UserTHPEntity(
    @PrimaryKey(autoGenerate = false) val personal_number: Long?,
    @ColumnInfo(name = "organization_number") val organization_number: Long? = null,
    @ColumnInfo(name = "initial") val initial: String? = null,
    @ColumnInfo(name = "department_name") val department_name: String? = null,
    @ColumnInfo(name = "position_name") val position_name: String? = null,
    @ColumnInfo(name = "employee_subgroup") val employee_subgroup: String? = null,
    @ColumnInfo(name = "startdate") val startdate: String? = null,
    @ColumnInfo(name = "enddate") val enddate: String? = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "status") val status: String? = null,
    @ColumnInfo(name = "first_name") val first_name: String? = null,
    @ColumnInfo(name = "last_name") val last_name: String? = null,
) : BaseModel()


data class UserTHPResponse(
    var status: String?,
    var data: List<UserTHPEntity>?
) : BaseModel()

fun getRandomName(): String {
    val names = listOf(
        "John",
        "Jane",
        "Alex",
        "Emily",
        "Chris",
        "Katie",
        "Michael",
        "Sarah",
        "David",
        "Laura"
    )
    return names[Random.nextInt(names.size)]
}

fun getRandomLastName(): String {
    val names = listOf(
        "John",
        "Jane",
        "Alex",
        "Emily",
        "Chris",
        "Katie",
        "Michael",
        "Sarah",
        "David",
        "Laura"
    )
    return names[Random.nextInt(names.size)]
}

fun getRandomEmail(name: String): String {
    val domains = listOf("example.com", "email.com", "mail.com", "test.com")
    return "$name@${domains[Random.nextInt(domains.size)]}".toLowerCase()
}

fun getRandomPhone(): String {
    return "0123456789".map { it }.shuffled().subList(0, 10).joinToString("")
}

fun getRandomDepartment(): String {
    val departments = listOf("HR", "Finance", "Engineering", "Marketing", "Sales", "Support")
    return departments[Random.nextInt(departments.size)]
}

fun getRandomPosition(): String {
    val positions =
        listOf("Manager", "Engineer", "Analyst", "Specialist", "Coordinator", "Assistant")
    return positions[Random.nextInt(positions.size)]
}

fun generateRandomUsers(count: Int): List<UserTHPEntity> {
    return List(count) {
        val name = getRandomName()
        UserTHPEntity(
            personal_number = it.toLong(),
            organization_number = it.toLong(),
            first_name = getRandomName(),
            last_name = getRandomLastName(),
            email = getRandomEmail(name),
            initial = Random.nextLong(1000, 9999).toString(),
            position_name = getRandomPosition()
        )
    }
}