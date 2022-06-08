package com.tools.files.myreader.model

class DataListMerge {
    var itemPDFModel: ItemPDFModel? = null
    var click: Boolean? = null

    constructor() {}
    constructor(itemPDFModel: ItemPDFModel?, isClick: Boolean?) {
        this.itemPDFModel = itemPDFModel
        click = isClick
    }

    constructor(itemPDFModel: ItemPDFModel?) {
        click = false
        this.itemPDFModel = itemPDFModel
    }
}