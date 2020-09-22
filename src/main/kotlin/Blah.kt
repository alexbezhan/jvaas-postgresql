package io.jvaas.sql.postgresql

class Blah(val con: com.github.jasync.sql.db.Connection) {

	// ========================================
	// 	tables
	// ========================================
	/*
	
		account {
			id -> accountId : String
			created -> accountCreated : java.time.LocalDateTime
			modified -> accountModified : java.time.LocalDateTime
			version -> accountVersion : Int
			display_name? -> accountDisplayName : String?
			email -> accountEmail : String
			enabled -> accountEnabled : Boolean
			full_name? -> accountFullName : String?
			hash? -> accountHash : String?
			verified -> accountVerified : Boolean
			verify_hash? -> accountVerifyHash : String?
			reset_hash? -> accountResetHash : String?
			phone? -> accountPhone : String?
		}
		listing {
			id -> listingId : String
			created? -> listingCreated : java.time.LocalDateTime?
			modified? -> listingModified : java.time.LocalDateTime?
			version? -> listingVersion : Int?
			title? -> listingTitle : String?
			description? -> listingDescription : String?
			country? -> listingCountry : String?
			province? -> listingProvince : String?
			city? -> listingCity : String?
			suburb? -> listingSuburb : String?
			country_url? -> listingCountryUrl : String?
			province_url? -> listingProvinceUrl : String?
			city_url? -> listingCityUrl : String?
			suburb_url? -> listingSuburbUrl : String?
			sale -> listingSale : Boolean
			rental -> listingRental : Boolean
			sale_price? -> listingSalePrice : Int?
			rental_price? -> listingRentalPrice : Int?
			title_url? -> listingTitleUrl : String?
			bedrooms? -> listingBedrooms : Int?
			bathrooms? -> listingBathrooms : Int?
			garages? -> listingGarages : Int?
			parkings? -> listingParkings : Int?
			floor_size? -> listingFloorSize : Long?
			erf_size? -> listingErfSize : Long?
			rates_and_taxes? -> listingRatesAndTaxes : Long?
			levies? -> listingLevies : Int?
			published -> listingPublished : Boolean
			section? -> listingSection : String?
		}
		listing_image {
			id -> listingImageId : String
			listing_id -> listingImageListingId : String
			width? -> listingImageWidth : Int?
			height? -> listingImageHeight : Int?
			name -> listingImageName : String
			batch -> listingImageBatch : String
		}
		session {
			created -> sessionCreated : java.time.LocalDateTime
			modified -> sessionModified : java.time.LocalDateTime
			version -> sessionVersion : Int
			token? -> sessionToken : String?
			account_id -> sessionAccountId : String
			email? -> sessionEmail : String?
			active -> sessionActive : Boolean
		}
	
	*/
	// ========================================
	// 	queries
	// ========================================
	
	private fun org.joda.time.LocalDateTime.fromJodaDateTimeToJavaLocalDateTime(): java.time.LocalDateTime {
		val utc = this.toDateTime(org.joda.time.DateTimeZone.UTC);
		val secondsSinceEpoch: Long = utc.millis / 1000
		val milliSeconds: Long = utc.millis - (secondsSinceEpoch * 1000)
	
		return java.time.LocalDateTime.ofEpochSecond(
			secondsSinceEpoch,
			milliSeconds.toInt() * 1000000,
			java.time.ZoneOffset.UTC
		)
	}
	
	// ========================================
	
	suspend fun insertListingId(
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			INSERT INTO listing ( id , created , modified ) 
			VALUES ( ? , now ( ) , now ( ) ) 
			""",
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun demoQuery(
		listingTitleUrl: String?, 
		listingId: String, 
		listingVersion: Int?, 
		listingTitle: String?, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , title_url 
			= ? WHERE id = ? AND version = ? AND title LIKE ? OR 
			bedrooms > 0 
			""",
			listingTitleUrl,
			listingId,
			listingVersion,
			listingTitle,
		)
	}
	
	// ========================================
	
	suspend fun updateModified(
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) WHERE id = ? 
			""",
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateTitle(
		listingTitle: String?, 
		listingTitleUrl: String?, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , title = ? , 
			title_url = ? WHERE id = ? 
			""",
			listingTitle,
			listingTitleUrl,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateDescription(
		listingDescription: String?, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , 
			description = ? WHERE id = ? 
			""",
			listingDescription,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateCounty(
		listingCountry: String?, 
		listingCountryUrl: String?, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , country = ? 
			, country_url = ? WHERE id = ? 
			""",
			listingCountry,
			listingCountryUrl,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateProvince(
		listingProvince: String?, 
		listingProvinceUrl: String?, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , province = 
			? , province_url = ? WHERE id = ? 
			""",
			listingProvince,
			listingProvinceUrl,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateCity(
		listingCity: String?, 
		listingCityUrl: String?, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , city = ? , 
			city_url = ? WHERE id = ? 
			""",
			listingCity,
			listingCityUrl,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateSuburb(
		listingSuburb: String?, 
		listingSuburbUrl: String?, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , suburb = ? , 
			suburb_url = ? WHERE id = ? 
			""",
			listingSuburb,
			listingSuburbUrl,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateSale(
		listingSale: Boolean, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , sale = ? 
			WHERE id = ? 
			""",
			listingSale,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateRental(
		listingRental: Boolean, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , rental = ? 
			WHERE id = ? 
			""",
			listingRental,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateSalePrice(
		listingSalePrice: Int?, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , 
			sale_price = ? WHERE id = ? 
			""",
			listingSalePrice,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateRentalPrice(
		listingRentalPrice: Int?, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , 
			rental_price = ? WHERE id = ? 
			""",
			listingRentalPrice,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateBedroomsCount(
		listingBedrooms: Int?, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , bedrooms = 
			? WHERE id = ? 
			""",
			listingBedrooms,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateBathroomsCount(
		listingBathrooms: Int?, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , bathrooms 
			= ? WHERE id = ? 
			""",
			listingBathrooms,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateGaragesCount(
		listingGarages: Int?, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , garages = ? 
			WHERE id = ? 
			""",
			listingGarages,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateParkingsCount(
		listingParkings: Int?, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , parkings = 
			? WHERE id = ? 
			""",
			listingParkings,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateFloorSize(
		listingFloorSize: Long?, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , 
			floor_size = ? WHERE id = ? 
			""",
			listingFloorSize,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateErfSize(
		listingErfSize: Long?, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , erf_size = 
			? WHERE id = ? 
			""",
			listingErfSize,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateRatesAndTaxes(
		listingRatesAndTaxes: Long?, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , 
			rates_and_taxes = ? WHERE id = ? 
			""",
			listingRatesAndTaxes,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updatePublished(
		listingPublished: Boolean, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , published 
			= ? WHERE id = ? 
			""",
			listingPublished,
			listingId,
		)
	}
	
	// ========================================
	
	suspend fun updateSection(
		listingSection: String?, 
		listingId: String, 
	) {
		con.execute(
			// language=SQL
			"""
			UPDATE listing SET modified = now ( ) , section = ? 
			WHERE id = ? 
			""",
			listingSection,
			listingId,
		)
	}
	
	// ========================================
	
	data class SelectAllWhereIdIs1Result(
		val listingId : String,
		val listingTitle : String?,
	)
	
	suspend fun selectAllWhereIdIs1(
		listingId: String, 
	): List<SelectAllWhereIdIs1Result> {
		return con.execute(
			// language=SQL
			"""
			SELECT l . id , title FROM listing AS l WHERE l . id = 
			? 
			""",
			listingId,
		).rows.map { row ->
			SelectAllWhereIdIs1Result(
				listingId = row.getString(0) ?: throw Exception(
					"listing.id is declared as non-nullable, but is returning null"
				),
				listingTitle = row.getString(1),
			)
		}
	}
	
	// ========================================
	
	data class ComplexSelectWithJoinResult(
		val listingId : String,
		val listingTitle : String?,
		val listingTitleUrl : String?,
		val listingImageId : String,
		val listingImageWidth : Int?,
		val listingImageHeight : Int?,
		val listingImageName : String,
		val listingPublished : Boolean,
		val listingParkings : Int?,
		val listingErfSize : Long?,
		val listingSalePrice : Int?,
		val listingCreated : java.time.LocalDateTime?,
		val listingModified : java.time.LocalDateTime?,
		val listingVersion : Int?,
	)
	
	suspend fun complexSelectWithJoin(
		listingId: String, 
		listingImageWidth: Int?, 
		listingImageHeight: Int?, 
	): List<ComplexSelectWithJoinResult> {
		return con.execute(
			// language=SQL
			"""
			SELECT l . id , l . title , l . title_url , li . id , li . 
			width , li . height , li . name , l . published , l . 
			parkings , l . erf_size , l . sale_price , l . created , l . 
			modified , l . version FROM listing AS l LEFT JOIN 
			listing_image AS li ON l . id = li . listing_id LEFT JOIN account 
			AS a ON a . id = l . id WHERE l . id = ? AND l . created < now ( 
			) AND li . width > ? AND li . height > ? 
			""",
			listingId,
			listingImageWidth,
			listingImageHeight,
		).rows.map { row ->
			ComplexSelectWithJoinResult(
				listingId = row.getString(0) ?: throw Exception(
					"listing.id is declared as non-nullable, but is returning null"
				),
				listingTitle = row.getString(1),
				listingTitleUrl = row.getString(2),
				listingImageId = row.getString(3) ?: throw Exception(
					"listing_image.id is declared as non-nullable, but is returning null"
				),
				listingImageWidth = row.getInt(4),
				listingImageHeight = row.getInt(5),
				listingImageName = row.getString(6) ?: throw Exception(
					"listing_image.name is declared as non-nullable, but is returning null"
				),
				listingPublished = row.getBoolean(7) ?: throw Exception(
					"listing.published is declared as non-nullable, but is returning null"
				),
				listingParkings = row.getInt(8),
				listingErfSize = row.getLong(9),
				listingSalePrice = row.getInt(10),
				listingCreated = row.getDate(11)?.fromJodaDateTimeToJavaLocalDateTime(),
				listingModified = row.getDate(12)?.fromJodaDateTimeToJavaLocalDateTime(),
				listingVersion = row.getInt(13),
			)
		}
	}
	
	// ========================================
	
}
