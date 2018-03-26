package com.shop440.features.checkout.kart

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.shop440.R
import com.shop440.features.checkout.models.ItemForKart
import com.shop440.utils.Metrics
import kotlinx.android.synthetic.main.fragment_item.view.*


class KartAdapter(val items:ArrayList<ItemForKart>, val listener:OnListFragmentInteractionListener) : RecyclerView.Adapter<KartAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(items[position])
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        fun onBind(item: ItemForKart){
            mView.itemName.text = item.itemName
            mView.shopName.text = item.shopName
            mView.quantityText.text = item.quantity.toString()
            mView.amountText.text = Metrics.getDisplayPriceWithCurrency(mView.context, item.amount)
            mView.removeItem.setOnClickListener {
                listener.onItemDelete(item)
            }
            mView.reduceItem.setOnClickListener {
                listener.onItemIncrement(item, false)
            }
            mView.addItem.setOnClickListener {
                listener.onItemIncrement(item, true)
            }

        }
    }

    interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onListFragmentInteraction(item: ItemForKart)
        fun onItemDelete(item: ItemForKart)
        fun onItemIncrement(item: ItemForKart, increment:Boolean)
    }
}
