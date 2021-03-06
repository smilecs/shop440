package com.shop440.features.checkout.viewholder

import android.view.View
import com.shop440.R
import com.shop440.features.checkout.models.SummaryAdapterModel
import com.shop440.BaseViewHolder
import com.shop440.utils.Metrics
import kotlinx.android.synthetic.main.summary_item_layout.view.*


/**
 * Created by mmumene on 10/02/2018.
 */
class SummaryViewHolder (val view: View) : BaseViewHolder<SummaryAdapterModel>(view){
    private val itemName = view.itemName
    private val quantityText = view.quantityText
    private val amountText = view.amountText
    override fun bind(item: SummaryAdapterModel, position: Int) {
        val kartItem = item.item[position]
        itemName.text = kartItem.itemName
        quantityText.text = view.context.getString(R.string.quantity, kartItem.quantity.toString())
        amountText.text = view.context.getString(R.string.amount, Metrics.getDisplayPriceWithCurrency(view.context, kartItem.amount))
    }
}