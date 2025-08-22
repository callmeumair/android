package com.commutetimely.core.domain.util

/**
 * Sealed class representing the result of an operation
 * 
 * This class provides a type-safe way to handle different states
 * of asynchronous operations including success, error, and loading states.
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    
    /**
     * Represents a successful operation with data
     */
    class Success<T>(data: T) : Resource<T>(data)
    
    /**
     * Represents an error state with optional error message
     */
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    
    /**
     * Represents a loading state
     */
    class Loading<T>(data: T? = null) : Resource<T>(data)
    
    /**
     * Check if the resource is in success state
     */
    fun isSuccess(): Boolean = this is Success
    
    /**
     * Check if the resource is in error state
     */
    fun isError(): Boolean = this is Error
    
    /**
     * Check if the resource is in loading state
     */
    fun isLoading(): Boolean = this is Loading
    

    
    /**
     * Get the error message if available, null otherwise
     */
    fun getErrorMessage(): String? = when (this) {
        is Success -> null
        is Error -> message
        is Loading -> null
    }
}
