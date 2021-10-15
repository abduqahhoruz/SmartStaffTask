package com.abduqahhor.qr_task

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.abduqahhor.qr_task.databinding.MainFragmentBinding
import com.google.zxing.WriterException

class MainFragment : Fragment(R.layout.main_fragment), AdapterView.OnItemSelectedListener {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private var bitmap: Bitmap? = null
    private var qrCode: QRGEncoder? = null
    private val listQR = mutableListOf<QRGEncoder>()
    private val handler by lazy { Handler() }
    private var speed = 1000L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val windowManager = requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val display = windowManager.defaultDisplay

        val point = Point()
        display.getRealSize(point)

        for (item in resources.getStringArray(R.array.algorithm)) {
            listQR.add(generateQR(item))
        }

        Log.d("logg", "onViewCreated: $listQR \n")
        Log.d("logg", "onViewCreated: ${listQR.size}")

        setupView()
        setupSpinner()

        binding.imageFirst.setOnClickListener {
            binding.imageFirst.setImageResource(R.color.black)
        }
        binding.imageSecond.setOnClickListener {
            binding.imageSecond.setImageResource(R.color.black)
        }
        binding.btnGenerate.setOnClickListener {
            for (pos in  listQR.size-1 downTo 0) {
                    binding.imageFirst.setImageBitmap(listQR[pos].encodeAsBitmap())
                    binding.imageSecond.setImageBitmap(listQR[pos].encodeAsBitmap())

            }
            binding.spinnerFirst.setSelection(0)
            binding.spinnerSecond.setSelection(0)
        }
    }


    private fun setupView() {
        binding.imageFirst.setImageBitmap(listQR[0].encodeAsBitmap())
        binding.imageSecond.setImageBitmap(listQR[0].encodeAsBitmap())
    }

    private fun generateQR(s: String): QRGEncoder {
        return QRGEncoder(s, null, QRGContents.Type.TEXT, 200)
    }

    private fun setOnImageView(id: Int, image: Bitmap) {
        try {
            if (id == R.id.spinner_first) {
                binding.imageFirst.setImageBitmap(image)
            } else {
                binding.imageSecond.setImageBitmap(image)
            }
        } catch (e: WriterException) {
            Log.e("Tag", e.toString())
        }
    }

    private fun setupSpinner() {
        val mutableListMarks = mutableListOf<String>()
        mutableListMarks.addAll(resources.getStringArray(R.array.algorithm))

        ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            mutableListMarks
        ).also { adapter ->

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.spinnerFirst.adapter = adapter
            binding.spinnerSecond.adapter = adapter
            binding.spinnerFirst.onItemSelectedListener = this
            binding.spinnerSecond.onItemSelectedListener = this
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        setOnImageView(parent!!.id, listQR[position].encodeAsBitmap())
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}