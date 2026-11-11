package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.Complaint
import kotlinx.coroutines.flow.Flow

@Dao
interface ComplaintDao {
    @Query("SELECT * FROM complaints ORDER BY createdAt DESC")
    fun getAllComplaints(): Flow<List<Complaint>>

    @Query("SELECT * FROM complaints WHERE id = :id LIMIT 1")
    suspend fun getComplaintById(id: Long): Complaint?

    @Query("SELECT * FROM complaints WHERE studentId = :studentId ORDER BY createdAt DESC")
    fun getComplaintsForStudent(studentId: Long): Flow<List<Complaint>>

    @Query("SELECT * FROM complaints WHERE status = :status ORDER BY createdAt DESC")
    fun getComplaintsByStatus(status: String): Flow<List<Complaint>>

    @Query("SELECT * FROM complaints WHERE severity = 'Critical' AND isResolved = 0 ORDER BY createdAt DESC")
    fun getActiveCriticalComplaints(): Flow<List<Complaint>>

    @Query("SELECT * FROM complaints WHERE isResolved = 0 ORDER BY createdAt DESC")
    fun getPendingComplaints(): Flow<List<Complaint>>

    @Query("SELECT COUNT(*) FROM complaints")
    fun getTotalComplaintsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM complaints WHERE isResolved = 0")
    fun getPendingComplaintsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM complaints WHERE isResolved = 1")
    fun getResolvedComplaintsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM complaints WHERE severity = 'Critical' AND isResolved = 0")
    fun getCriticalComplaintsCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaint(complaint: Complaint): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComplaints(complaints: List<Complaint>)

    @Update
    suspend fun updateComplaint(complaint: Complaint)

    @Delete
    suspend fun deleteComplaint(complaint: Complaint)
}
