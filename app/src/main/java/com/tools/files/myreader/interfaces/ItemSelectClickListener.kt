package com.tools.files.myreader.interfaces

import com.tools.files.myreader.model.ItemPDFModel

interface ItemSelectClickListener {
    fun onSplitClick(itemPDFModel: ItemPDFModel)
    fun onExtractTextClick(itemPDFModel: ItemPDFModel)
    fun onExtractImageClick(itemPDFModel: ItemPDFModel)
    fun onPDFToImageClick(itemPDFModel: ItemPDFModel)
    fun onCompressClick(itemPDFModel: ItemPDFModel)
    fun onRemovePageClick(itemPDFModel: ItemPDFModel)
    fun onOpenPDF(itemPDFModel: ItemPDFModel)
//    fun onMergePDF(itemPDFModel: ItemPDFModel)

}