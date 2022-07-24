package com.pachuho.sleepAlarm.support

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import sleepAlarm.databinding.ItemAlarmBinding

abstract class AlarmAdapter : ListAdapter<Alarm, AlarmAdapter.AlarmItemViewHolder>(diffUtil) {
    abstract fun checkUsing(id: Int, use: Boolean)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmItemViewHolder {
        return AlarmItemViewHolder(ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AlarmItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class AlarmItemViewHolder(private val binding: ItemAlarmBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(alarm: Alarm) = with(binding){
            tvTime.text = alarm.timeText
            tbUse.isChecked = alarm.use

            tbUse.setOnCheckedChangeListener { _, isChecked -> checkUsing(alarm.id, isChecked)}
        }
    }

    companion object{
        val diffUtil= object: DiffUtil.ItemCallback<Alarm>(){
            override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm) =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm) =
                oldItem == newItem
        }
    }
}