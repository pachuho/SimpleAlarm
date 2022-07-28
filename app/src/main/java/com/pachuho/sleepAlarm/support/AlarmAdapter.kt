package com.pachuho.sleepAlarm.support

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pachuho.sleepAlarm.data.datasource.model.Alarm
import sleepAlarm.R
import sleepAlarm.databinding.ItemAlarmBinding

abstract class AlarmAdapter : ListAdapter<Alarm, AlarmAdapter.AlarmItemViewHolder>(diffUtil) {
    abstract fun onCheckUsing(id: Int, use: Boolean)
    abstract fun onDeleteAlarm(alarm: Alarm)
    abstract fun onUpdateAlarm(alarm: Alarm)

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
            tvApPm.text = if(alarm.hour < 12) "오전" else "오후"

            clAlarm.setOnClickListener {
                onUpdateAlarm(alarm)
            }
            tbUse.setOnCheckedChangeListener { _, isChecked -> onCheckUsing(alarm.id, isChecked)}
            ivSubMenu.setOnClickListener {
                val popup = PopupMenu(ivSubMenu.context, ivSubMenu)
                popup.inflate(R.menu.alarm_list_menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.action_remove -> removeItem(alarm)
                    }
                    true
                }
                popup.show()
            }
        }
    }

    private fun removeItem(alarm: Alarm){
        val currentList = currentList.toMutableList()
        currentList.remove(alarm)
        submitList(currentList)
        onDeleteAlarm(alarm)
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