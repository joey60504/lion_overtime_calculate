package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemOverTimeBinding

class OverTimeAdapter(
    private val dataList: ArrayList<OvertimeRecord>,
    private val commonPrice: Double,
    private val specialPrice: Double
) : RecyclerView.Adapter<OverTimeAdapter.SelfGuidedTourOrderDetailHotelViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SelfGuidedTourOrderDetailHotelViewHolder {
        return SelfGuidedTourOrderDetailHotelViewHolder(
            ItemOverTimeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: SelfGuidedTourOrderDetailHotelViewHolder, position: Int) {
        holder.bind(dataList[position], commonPrice, specialPrice)
    }

    inner class SelfGuidedTourOrderDetailHotelViewHolder(private val binding: ItemOverTimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: OvertimeRecord, commonPrice: Double, specialPrice: Double) {
            binding.textView4.text = data.date
            binding.textView5.text = "${data.recognitionHours}h"

            if ((data.recognitionHours ?: 0.0) < 2.0) {
                binding.textView6.text =
                    "兩小時內：$${((data.recognitionHours ?: 0.0) * commonPrice)}"
                binding.textView7.text =
                    "兩小時外：$0"
            } else {
                binding.textView6.text =
                    "兩小時內：$${(2.0 * commonPrice)}"
                binding.textView7.text =
                    "兩小時外：$${((data.recognitionHours ?: 0.0) - 2.0) * specialPrice}"
            }
        }
    }
}