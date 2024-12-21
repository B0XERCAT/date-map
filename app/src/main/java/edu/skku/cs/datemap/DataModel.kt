package edu.skku.cs.datemap

data class DataModel(var items: List<Item>) {
    data class Item(
        var title: String,
        var link: String,
        var address: String,
        var mapx: String,
        var mapy: String,
        var category: String
    )
}
