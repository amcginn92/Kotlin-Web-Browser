package edu.temple.superbrowser

import java.io.Serializable

data class Bookmark(val url: String, val title: String): Serializable{
    override fun equals(other: Any?): Boolean {
        if(!(other is Bookmark)){
            return false
        }
        if(this.url == other.url && this.title == other.title){
            return true
        }
        return false
    }
}
