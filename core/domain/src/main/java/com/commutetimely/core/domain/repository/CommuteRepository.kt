package com.commutetimely.core.domain.repository

import com.commutetimely.core.domain.model.Commute
import com.commutetimely.core.domain.model.Location
import com.commutetimely.core.domain.model.RouteType
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for commute-related operations
 * 
 * This interface defines the contract for all commute data operations
 * including CRUD operations, route planning, and real-time updates.
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
interface CommuteRepository {
    
    /**
     * Get all saved commutes as a Flow
     * 
     * @return Flow of list of commutes
     */
    fun getAllCommutes(): Flow<List<Commute>>
    
    /**
     * Get a specific commute by ID
     * 
     * @param id The unique identifier of the commute
     * @return The commute if found, null otherwise
     */
    suspend fun getCommuteById(id: String): Commute?
    
    /**
     * Save a new commute
     * 
     * @param commute The commute to save
     * @return The saved commute with generated ID
     */
    suspend fun saveCommute(commute: Commute): Commute
    
    /**
     * Update an existing commute
     * 
     * @param commute The updated commute
     * @return The updated commute
     */
    suspend fun updateCommute(commute: Commute): Commute
    
    /**
     * Delete a commute
     * 
     * @param id The ID of the commute to delete
     */
    suspend fun deleteCommute(id: String)
    
    /**
     * Plan a new route between two locations
     * 
     * @param origin Starting location
     * @param destination End location
     * @param routeType Preferred route type
     * @return Planned commute with route details
     */
    suspend fun planRoute(
        origin: Location,
        destination: Location,
        routeType: RouteType
    ): Commute
    
    /**
     * Get real-time traffic updates for a commute
     * 
     * @param commuteId The ID of the commute to update
     * @return Updated commute with current traffic information
     */
    suspend fun getTrafficUpdates(commuteId: String): Commute
    
    /**
     * Get active commutes
     * 
     * @return Flow of currently active commutes
     */
    fun getActiveCommutes(): Flow<List<Commute>>
    
    /**
     * Start tracking a commute
     * 
     * @param commuteId The ID of the commute to start tracking
     */
    suspend fun startCommute(commuteId: String)
    
    /**
     * Stop tracking a commute
     * 
     * @param commuteId The ID of the commute to stop tracking
     */
    suspend fun stopCommute(commuteId: String)
}
