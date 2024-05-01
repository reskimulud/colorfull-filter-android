package io.github.reskimulud.colorfulfilterapp

import android.graphics.Bitmap
import android.graphics.Color
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.math.sqrt

class ImageFiltering {
    companion object {
        const val BRIGHTER_FACTOR = 20.0
    }

    suspend fun filterColorfulImageFromBitmap(bitmap: Bitmap, isBrighter: Boolean = false, onSuccess: (Bitmap) -> Unit) {
        withContext(Dispatchers.IO) {
            val deferredFiltering = async {
                val width = bitmap.width
                val height = bitmap.height
                val resultBitmap = Bitmap.createBitmap(width, height, bitmap.config)

                for (x in 0 until width) {
                    for (y in 0 until height) {
                        val pixel = bitmap.getPixel(x, y)
                        val red = Color.red(pixel)
                        val green = Color.green(pixel)
                        val blue = Color.blue(pixel)

                        val currentPixel = calculateRGB(red, green, blue)

                        // pixel yang kurang colorful
                        val p1 = calculateRGB(67, 76, 73)
                        val p2 = calculateRGB(99, 105, 93)
                        val p3 = calculateRGB(178, 173, 169)
                        val p4 = calculateRGB(22, 20, 18)
                        val p5 = calculateRGB(10, 40, 50)
                        val p6 = calculateRGB(140, 132, 139)
                        val p7 = calculateRGB(87, 76, 63)
                        val p8 = calculateRGB(173, 166, 167)
                        val p9 = calculateRGB(48, 35, 46)


                        // Tentukan apakah pixel tersebut colorful atau tidak
                        when (currentPixel) {
                            p1 -> {
                                val brighterPixel = if (isBrighter) pixel.brighterPixel(BRIGHTER_FACTOR) else Color.WHITE
                                resultBitmap.setPixel(x, y, brighterPixel)
                            }
                            p2 -> {
                                val brighterPixel = if (isBrighter) pixel.brighterPixel(BRIGHTER_FACTOR) else Color.WHITE
                                resultBitmap.setPixel(x, y, brighterPixel)
                            }
                            p3 -> {
                                val brighterPixel = if (isBrighter) pixel.brighterPixel(BRIGHTER_FACTOR) else Color.WHITE
                                resultBitmap.setPixel(x, y, brighterPixel)
                            }
                            p4 -> {
                                val brighterPixel = if (isBrighter) pixel.brighterPixel(BRIGHTER_FACTOR) else Color.WHITE
                                resultBitmap.setPixel(x, y, brighterPixel)
                            }
                            p5 -> {
                                val brighterPixel = if (isBrighter) pixel.brighterPixel(BRIGHTER_FACTOR) else Color.WHITE
                                resultBitmap.setPixel(x, y, brighterPixel)
                            }
                            p6 -> {
                                val brighterPixel = if (isBrighter) pixel.brighterPixel(BRIGHTER_FACTOR) else Color.WHITE
                                resultBitmap.setPixel(x, y, brighterPixel)
                            }
                            p7 -> {
                                val brighterPixel = if (isBrighter) pixel.brighterPixel(BRIGHTER_FACTOR) else Color.WHITE
                                resultBitmap.setPixel(x, y, brighterPixel)
                            }
                            p8 -> {
                                val brighterPixel = if (isBrighter) pixel.brighterPixel(BRIGHTER_FACTOR) else Color.WHITE
                                resultBitmap.setPixel(x, y, brighterPixel)
                            }
                            p9 -> {
                                val brighterPixel = if (isBrighter) pixel.brighterPixel(BRIGHTER_FACTOR) else Color.WHITE
                                resultBitmap.setPixel(x, y, brighterPixel)
                            }
                            else -> resultBitmap.setPixel(x, y, pixel)
                        }
                    }
                }

                return@async resultBitmap
            }

            onSuccess.invoke(deferredFiltering.await())
        }
    }

    private fun calculateRGB(red: Int, green: Int, blue: Int): Double =
        sqrt(
            (red - 255).toDouble().pow(2) +
                    (green - 255).toDouble().pow(2) +
                    (blue - 255).toDouble().pow(2)
        )

    private fun Int.brighterPixel(factor: Double): Int {
        val red = Color.red(this)
        val green = Color.green(this)
        val blue = Color.blue(this)

        return Color.rgb(
            red.brighterColor(factor),
            green.brighterColor(factor),
            blue.brighterColor(factor)
        )
    }

    private fun Int.brighterColor(factor: Double): Int = (this * factor).toInt().coerceAtMost(255)
}