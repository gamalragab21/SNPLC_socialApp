package gamal.myappnew.com.socialappx.repositories

import gamal.myappnew.com.socialappx.other.Resource

inline fun <T> safeCall(action: () -> Resource<T>): Resource<T> {
    return try {
        action()
    } catch(e: Exception) {
        Resource.Error(e.message ?: "An unknown error occured")
    }
}