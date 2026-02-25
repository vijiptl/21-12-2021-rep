package com.jobsniper.ai.ui.jobs

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jobsniper.ai.databinding.ItemJobBinding

class JobAdapter(
    private val onOpenClicked: (String) -> Unit
) : ListAdapter<JobItemUi, JobAdapter.JobVH>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobVH {
        val binding = ItemJobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobVH(binding)
    }

    override fun onBindViewHolder(holder: JobVH, position: Int) {
        holder.bind(getItem(position), onOpenClicked)
    }

    class JobVH(private val binding: ItemJobBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: JobItemUi, onOpenClicked: (String) -> Unit) {
            binding.role.text = item.title
            binding.company.text = "${item.company} • ${item.postedAgo}"
            binding.bullets.text = item.bullets
            binding.recruiterMessage.text = item.recruiterMessage
            binding.screening.text = item.screeningAnswers
            binding.coverNote.text = item.coverNote

            binding.openLinkedin.setOnClickListener {
                onOpenClicked(item.id)
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.jobUrl))
                itemView.context.startActivity(intent)
            }

            binding.copyAnswers.setOnClickListener {
                val text = """
                    Resume Bullets:
                    ${item.bullets}

                    Recruiter Message:
                    ${item.recruiterMessage}

                    Screening Answers:
                    ${item.screeningAnswers}

                    Cover Note:
                    ${item.coverNote}
                """.trimIndent()
                copy(itemView.context, text)
            }
        }

        private fun copy(context: Context, value: String) {
            val manager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            manager.setPrimaryClip(ClipData.newPlainText("JobSniper Answers", value))
            Toast.makeText(context, "Tailored content copied", Toast.LENGTH_SHORT).show()
        }
    }

    object Diff : DiffUtil.ItemCallback<JobItemUi>() {
        override fun areItemsTheSame(oldItem: JobItemUi, newItem: JobItemUi): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: JobItemUi, newItem: JobItemUi): Boolean = oldItem == newItem
    }
}
