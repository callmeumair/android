package com.commutetimely.core.domain.usecase

import com.commutetimely.core.domain.model.Commute
import com.commutetimely.core.domain.repository.CommuteRepository
import com.commutetimely.core.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case for retrieving commutes
 * 
 * This use case encapsulates the business logic for fetching commute data,
 * including error handling and data transformation.
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
class GetCommutesUseCase @Inject constructor(
    private val repository: CommuteRepository
) {
    
    /**
     * Execute the use case to get all commutes
     * 
     * @return Flow of Resource containing list of commutes or error
     */
    operator fun invoke(): Flow<Resource<List<Commute>>> {
        return repository.getAllCommutes().map { commutes ->
            try {
                Resource.Success(commutes)
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Unknown error occurred")
            }
        }
    }
    
    /**
     * Get commutes filtered by active status
     * 
     * @param activeOnly Whether to return only active commutes
     * @return Flow of Resource containing filtered commutes
     */
    fun getCommutes(activeOnly: Boolean = false): Flow<Resource<List<Commute>>> {
        return if (activeOnly) {
            repository.getActiveCommutes().map { commutes ->
                try {
                    Resource.Success(commutes)
                } catch (e: Exception) {
                    Resource.Error(e.message ?: "Unknown error occurred")
                }
            }
        } else {
            invoke()
        }
    }
    
    /**
     * Get a specific commute by ID
     * 
     * @param id The unique identifier of the commute
     * @return Resource containing the commute or error
     */
    suspend fun getCommuteById(id: String): Resource<Commute> {
        return try {
            val commute = repository.getCommuteById(id)
            if (commute != null) {
                Resource.Success(commute)
            } else {
                Resource.Error("Commute not found")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error occurred")
        }
    }
}
