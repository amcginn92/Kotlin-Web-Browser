package edu.temple.superbrowser

class BookMarkList():java.io.Serializable {

    val list = ArrayList<Bookmark>()
    var size = list.size

    fun add(url: String, title:String){
        this.list.add(Bookmark(url,title))
    }
    fun size(): Int {
        return list.size
    }

}