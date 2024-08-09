package com.cuongngo.core_project.data.database.roomdb.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.cuongngo.core_project.response.BaseModel
import kotlin.random.Random

@Entity(
    tableName = "users",
    indices = [Index(value = ["msnv"], unique = true)],
)
data class UserTHPEntity(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    @ColumnInfo(name = "status") val status: Int? = null,
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "email") val email: String? = null,
    @ColumnInfo(name = "phone") var phone: String? = null,
    @ColumnInfo(name = "msnv") var msnv: Long? = null,
    @ColumnInfo(name = "device_id") var deviceID: Long? = null,
    @ColumnInfo(name = "department") var department: String? = null,
    @ColumnInfo(name = "position") var position: String? = null,
) : BaseModel()

fun getRandomName(): String {
    val names = listOf("John", "Jane", "Alex", "Emily", "Chris", "Katie", "Michael", "Sarah", "David", "Laura")
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
    val positions = listOf("Manager", "Engineer", "Analyst", "Specialist", "Coordinator", "Assistant")
    return positions[Random.nextInt(positions.size)]
}

fun generateRandomUsers(count: Int): List<UserTHPEntity> {
    return List(count) {
        val name = getRandomName()
        UserTHPEntity(
            id = it.toLong(),
            status = Random.nextInt(0, 2),
            name = name,
            email = getRandomEmail(name),
            phone = getRandomPhone(),
            msnv = Random.nextLong(1000, 9999),
            deviceID = Random.nextLong(10000, 99999),
            department = getRandomDepartment(),
            position = getRandomPosition()
        )
    }
}