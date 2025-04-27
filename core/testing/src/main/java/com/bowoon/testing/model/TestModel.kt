package com.bowoon.testing.model

import com.bowoon.model.Paging
import com.bowoon.model.Product
import com.bowoon.model.Products
import com.bowoon.model.Section
import com.bowoon.model.SectionInfo

val testSectionInfo = SectionInfo(
    data = listOf(
        Section(
            title = "sectionTitle1",
            id = 0,
            type = "vertical",
            url = "/sectionImageUrl1.png",
            products = Products(
                data = listOf(
                    Product(
                        id = 1,
                        name = "section1_productName1",
                        image = "/section1_productImageUrl1.png",
                        originalPrice = 23000,
                        discountedPrice = null,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    Product(
                        id = 2,
                        name = "section1_productName2",
                        image = "/section1_productImageUrl2.jpg",
                        originalPrice = 63000,
                        discountedPrice = 34700,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    Product(
                        id = 3,
                        name = "section1_productName3",
                        image = "/section1_productImageUrl3.png",
                        originalPrice = 3850,
                        discountedPrice = 3500,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    Product(
                        id = 4,
                        name = "section1_productName4",
                        image = "/section1_productImageUrl4.jpg",
                        originalPrice = 67000,
                        discountedPrice = 58900,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    Product(
                        id = 5,
                        name = "section1_productName5",
                        image = "/section1_productImageUrl5.png",
                        originalPrice = 10000,
                        discountedPrice = 7900,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    Product(
                        id = 6,
                        name = "section1_productName6",
                        image = "/section1_productImageUrl6.jpg",
                        originalPrice = 36700,
                        discountedPrice = null,
                        isSoldOut = false,
                        isFavorite = false
                    )
                )
            )
        ),
        Section(
            title = "sectionTitle2",
            id = 1,
            type = "horizontal",
            url = "/sectionImageUrl2.png",
            products = Products(
                data = listOf(
                    Product(
                        id = 7,
                        name = "section2_productName7",
                        image = "/section2_productImageUrl7.png",
                        originalPrice = 3000,
                        discountedPrice = 2000,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    Product(
                        id = 8,
                        name = "section2_productName8",
                        image = "/section2_productImageUr8.jpg",
                        originalPrice = 13800,
                        discountedPrice = 9800,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    Product(
                        id = 9,
                        name = "section2_productName9",
                        image = "/section2_productImageUrl9.png",
                        originalPrice = 100000,
                        discountedPrice = 58600,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    Product(
                        id = 10,
                        name = "section2_productName10",
                        image = "/section2_productImageUr10.jpg",
                        originalPrice = 6700,
                        discountedPrice = null,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    Product(
                        id = 11,
                        name = "section2_productName11",
                        image = "/section2_productImageUrl11.png",
                        originalPrice = 7800,
                        discountedPrice = 5600,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    Product(
                        id = 12,
                        name = "section2_productName12",
                        image = "/section2_productImageUr12.jpg",
                        originalPrice = 7200,
                        discountedPrice = 5000,
                        isSoldOut = false,
                        isFavorite = false
                    )
                )
            )
        ),
        Section(
            title = "sectionTitle3",
            id = 2,
            type = "grid",
            url = "/sectionImageUrl3.jpg",
            products = Products(
                data = listOf(
                    Product(
                        id = 13,
                        name = "section3_productName13",
                        image = "/section3_productImageUrl13.png",
                        originalPrice = 38000,
                        discountedPrice = 30000,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    Product(
                        id = 14,
                        name = "section3_productName14",
                        image = "/section3_productImageUrl14.jpg",
                        originalPrice = 4000,
                        discountedPrice = 1800,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    Product(
                        id = 15,
                        name = "section3_productName15",
                        image = "/section3_productImageUrl15.png",
                        originalPrice = 7130,
                        discountedPrice = 2980,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    Product(
                        id = 16,
                        name = "section3_productName16",
                        image = "/section3_productImageUrl16.jpg",
                        originalPrice = 130000,
                        discountedPrice = 99000,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    Product(
                        id = 17,
                        name = "section3_productName17",
                        image = "/section3_productImageUrl17.png",
                        originalPrice = 125000,
                        discountedPrice = null,
                        isSoldOut = false,
                        isFavorite = false
                    ),
                    Product(
                        id = 18,
                        name = "section3_productName18",
                        image = "/section3_productImageUrl18.jpg",
                        originalPrice = 98000,
                        discountedPrice = 53700,
                        isSoldOut = false,
                        isFavorite = false
                    )
                )
            )
        )
    ),
    paging = Paging(
        nextPage = null
    )
)