package br.com.phaneronsoft.flickergallery.presentation.view.activity

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.phaneronsoft.flickergallery.R
import br.com.phaneronsoft.flickergallery.databinding.ActivityMainBinding
import br.com.phaneronsoft.flickergallery.databinding.DialogLargeImageBinding
import br.com.phaneronsoft.flickergallery.model.vo.PhotoVO
import br.com.phaneronsoft.flickergallery.presentation.view.adapter.PhotoAdapter
import br.com.phaneronsoft.flickergallery.presentation.viewmodel.MainViewModel
import br.com.phaneronsoft.flickergallery.utils.Constants
import br.com.phaneronsoft.flickergallery.utils.OnItemClickListener
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModel()
    lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var mAdapter: PhotoAdapter

    val tag = "dogs"
    var photoList = mutableListOf<PhotoVO>()
    var loading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.prepareUi()
        this.prepareObserver()
        this.prepareListener()

        viewModel.fetchPhotoList(tag)
        loading = true
    }

    private fun prepareUi() {
        mLayoutManager = GridLayoutManager(this@MainActivity, 2)
        mAdapter = PhotoAdapter(photoList)

        binding.recyclerViewGallery.layoutManager = mLayoutManager
        binding.recyclerViewGallery.adapter = mAdapter
        binding.recyclerViewGallery.setHasFixedSize(true)
    }

    private fun prepareObserver() {
        viewModel.photoList.observe(this, { photoListResponse ->
            this.updatePhotoSizeList(photoListResponse)

            if (photoList.isNotEmpty()) {
                photoList.removeLast()
                mAdapter.notifyItemRemoved(photoList.size - 1)
            }
            photoList.addAll(photoListResponse)

            if (viewModel.hasMoreData) {
                photoList.add(PhotoVO(Constants.TYPE_LOADING))
            }

            mAdapter.notifyDataSetChanged()

            binding.relativeLayoutLoading.visibility = View.GONE
            loading = false
        })

        viewModel.error.observe(this, { message ->
            binding.relativeLayoutLoading.visibility = View.GONE
            Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        })

        viewModel.updatedPhoto.observe(this, { message ->
            mAdapter.notifyDataSetChanged()
        })
    }

    private fun updatePhotoSizeList(list: List<PhotoVO>) {
        list.forEach { photo ->
            viewModel.fetchPhotoSizeList(photo)
        }
    }

    private fun prepareListener() {
        binding.recyclerViewGallery.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0 && !loading && viewModel.hasMoreData) {
                    if (mLayoutManager.findLastVisibleItemPosition() >= photoList.size - 1) {
                        viewModel.fetchPhotoList(tag)
                        loading = true
                    }
                }
            }
        })

        mAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(position: Int) {
                showLargePhoto(position, photoList[position])
            }
        })
    }

    fun showLargePhoto(position: Int, photo: PhotoVO) {
        try {
            val builder = Dialog(this, R.style.myDialogTheme)
            builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
            builder.window?.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )

            val dialogBinding = DialogLargeImageBinding.inflate(LayoutInflater.from(this))
            dialogBinding.imageViewPrevious.visibility = View.GONE
            dialogBinding.imageViewNext.visibility = View.GONE

            Picasso.get()
                .load(photo.getLargeImage()?.source)
                .placeholder(R.drawable.ic_image_outline)
                .error(R.drawable.ic_image_outline)
                .into(dialogBinding.imageViewPhoto, object : Callback {
                    override fun onSuccess() {
                        dialogBinding.imageViewPrevious.visibility = View.VISIBLE
                        dialogBinding.imageViewNext.visibility = View.VISIBLE
                    }

                    override fun onError(e: java.lang.Exception?) {
                        dialogBinding.imageViewPrevious.visibility = View.VISIBLE
                        dialogBinding.imageViewNext.visibility = View.VISIBLE
                    }
                })

            dialogBinding.textViewTitle.text = photo.title

            dialogBinding.imageViewPrevious.setOnClickListener {
                val newPosition = position - 1
                if (newPosition >= 0) {
                    showLargePhoto(newPosition, photoList[newPosition])
                    builder.dismiss()
                }
            }
            dialogBinding.imageViewNext.setOnClickListener {
                val newPosition = position + 1
                if (newPosition < photoList.size - 1) {
                    showLargePhoto(newPosition, photoList[newPosition])
                    builder.dismiss()
                }
            }

            builder.addContentView(
                dialogBinding.root, RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            builder.show()
        } catch (e: Exception) {
            Log.e(javaClass.simpleName, e.message, e)
        }
    }
}
