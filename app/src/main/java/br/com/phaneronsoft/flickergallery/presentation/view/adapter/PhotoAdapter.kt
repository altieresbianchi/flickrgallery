package br.com.phaneronsoft.flickergallery.presentation.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.phaneronsoft.flickergallery.R
import br.com.phaneronsoft.flickergallery.databinding.RecyclerItemLoadingBinding
import br.com.phaneronsoft.flickergallery.databinding.RecyclerItemPhotoBinding
import br.com.phaneronsoft.flickergallery.model.vo.PhotoVO
import br.com.phaneronsoft.flickergallery.utils.Constants
import br.com.phaneronsoft.flickergallery.utils.OnItemClickListener
import com.squareup.picasso.Picasso

class PhotoAdapter(
    private var items: MutableList<PhotoVO>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val VIEW_TYPE_PHOTO = 0
    val VIEW_TYPE_LOADING = 1

    private var mItemClickListener: OnItemClickListener? = null

    override fun getItemViewType(position: Int): Int {
        return if (items[position].id == Constants.TYPE_LOADING) {
            VIEW_TYPE_LOADING
        } else {
            VIEW_TYPE_PHOTO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_LOADING) {
            LoadingViewHolder(
                RecyclerItemLoadingBinding.inflate(LayoutInflater.from(parent.context))
            )
        } else {
            PhotoViewHolder(
                RecyclerItemPhotoBinding.inflate(LayoutInflater.from(parent.context)),
                mItemClickListener
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val photo = items[position]
        if (holder is LoadingViewHolder) {
            holder.bind()
        } else if (holder is PhotoViewHolder) {
            holder.bind(photo)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateList(newItems: MutableList<PhotoVO>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(mItemClickListener: OnItemClickListener) {
        this.mItemClickListener = mItemClickListener
    }
}

class PhotoViewHolder(
    private val binding: RecyclerItemPhotoBinding,
    private val listener: OnItemClickListener?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: PhotoVO) {
        if (item.getThumbImage() != null) {
            Picasso.get()
                .load(item.getThumbImage()?.source)
                .placeholder(R.drawable.ic_image_outline)
                .error(R.drawable.ic_image_outline)
                .into(binding.imageViewPhoto)
        }

        binding.root.setOnClickListener {
            listener?.onItemClick(layoutPosition)
        }
    }
}

class LoadingViewHolder(
    private val binding: RecyclerItemLoadingBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind() {
        binding.progressBarLoading.isIndeterminate = true
    }
}

