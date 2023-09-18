package com.voltor.futureleave.localization;

/**
 * Contains all localization keys used to i18n server-side-generated messages.
 */
public final class LocalizationKeys {
	private LocalizationKeys() {}
	
    public static final String DRIVER_CREATED = "driver_was_created";
    public static final String DRIVER_GROUP_CREATED = "driver_group_was_created";
    public static final String ACTIVITY_TYPE_CREATED = "activity_type_was_created";
    public static final String SALARY_VARIABLE_GROUP_CREATED = "salary_variable_group_was_created";
    public static final String VEHICLE_GROUP_CREATED = "vehicle_group_was_created";
    public static final String VEHICLE_CREATED = "vehicle_was_created";
    public static final String TRIP_CREATED = "trip_was_created";
    public static final String CURRENCY_CREATED = "currency_was_created";
    
    public static final String DRIVER_DELETED = "driver_was_deleted";
	public static final String VEHICLE_DELETED = "vehicle_was_deleted";
	public static final String ACTIVITY_TYPE_DELETED = "activity_type_was_deleted";
    
    public static final String FAILED_PROCESS_ACTIVITY = "failed_process_activity";
    public static final String FAILED_PROCESS_DRIVER = "failed_process_driver";
    public static final String FAILED_PROCESS_VEHICLE = "failed_process_vehicle";
    public static final String FAILED_PROCESS_ACTIVITY_TYPE = "failed_process_activity_type";
    public static final String FAILED_PROCESSING_ACTIVITIES = "failed_processing_activities";
    
    public static final String IMPORT_STOPPED_FAILURE = "import_stopped_failure";
    public static final String EXPORT_STOPPED_FAILURE = "export_stopped_failure";
    
    public static final String FAILED_TO_CREATE_ACTIVITIES_FOR_DRIVER = "failed_to_create_activities_for_driver";
    public static final String FAILED_TO_CREATE_ACTIVITY_FROM_IMPORTED_ACTIVITY = "failed_to_create_activity_from_imported_activity";
  
    public static final String STARTING_PROCESSING_IMPORTED_ACTIVITIES_FOR_DRIVER = "starting_processing_imported_activities_for_driver";
    public static final String FINISHED_PROCESSING_IMPORTED_ACTIVITIES_FOR_DRIVER = "finished_processing_imported_activities_for_driver";
    
    
    public static final String ACTIVITY_INCORRECT_ODOMETER = "activity_incorrect_odometer";
	public static final String ACTIVITIES_PAIR_INCORRECT_ODOMETER = "activities_pair_incorrect_odometer";
	public static final String ACTIVITY_INCORRECT_DURATION = "activity_incorrect_duration";
	
	public static final String STARTING_TRIP_GENERATION = "starting_trip_generation";
	public static final String FINISHED_TRIP_GENERATION = "finished_trip_generation";
	
	public static final String STARTING_AUTOMATIC_POI_ASSIGN = "starting_automatic_poi_assign";
	public static final String FINISHED_AUTOMATIC_POI_ASSIGN = "finished_automatic_poi_assign";
	
	public static final String STARTING_MANUAL_POI_ASSIGN = "starting_manual_poi_assign";
	public static final String FINISHED_MANUAL_POI_ASSIGN = "finished_manual_poi_assign";
	
	public static final String FAILED_AUTOMATIC_POI_ASSIGN = "failed_automatic_poi_assign";
	public static final String FAILED_MANUAL_POI_ASSIGN = "failed_manual_poi_assign";
	
	public static final String STARTING_IMPORT = "starting_import";
	public static final String FINISHED_IMPORT = "finished_import";
	public static final String STARTING_EXPORT = "starting_export";
	public static final String FINISHED_EXPORT = "finished_export";
	
	public static final String STOPPING_IMPORT_ITERATION_LIMIT_EXCEEDED = "stopping_import_iteration_limit_exceeded";
	
	public static final String TRIP_GENERATION_ERROR_CODE = "trip_generation_error_code";
	
	public static final String POI_ASSIGN_ERROR_CODE = "poi_assign_error_code";
	
	public static final String ACTIVITY_VALIDATION_ERROR_CODE = "activity_validation_error_code";
	
	public static final String IMPORT_ERROR_CODE = "import_error_code";
	public static final String EXPORT_CODE = "export_code";
	
	public static final String DEPENDENT_OBJECT_CREATION_ERROR_CODE = "dependent_object_creation_error_code";
	public static final String DEPENDENT_OBJECT_DELETE_ERROR_CODE = "dependent_object_delete_error_code";
	
	public static final String NON_TRIP_ACTIVITY_OVERLAP ="non_trip_activity_overlap";
	
	public static final String ACTIVITY_OVERLAP ="activity_overlap";
	
	public static final String SBB_PHASE2_EMPTY_RESPONSE = "sbb_phase2_empty_response";
	
	/**
	 * Indicate error situation - when marker in response is equal to request
	 */
	public static final String CC_MARKER_IN_RESPONSE_EQUAL_REQUEST = "cc_marker_in_response_equal_request";
	
	public static final String SAVED_IMPORTED_ACTIVITY_FOR_OUT_OF_DATE_DRIVER = "saved_imported_activity_for_out_of_date_driver";
	public static final String SAVED_IMPORTED_ACTIVITY_FOR_OUT_OF_DATE_VEHICLE = "saved_imported_activity_for_out_of_date_vehicle";
	
	public static final String EASY_TRACK_SUCCESSFULLY_DOWNLOADED_FILES_QUANTITY = "EasyTrackImportHandler.SuccessfullyDownloadedFilesQuantity";
	public static final String EASY_TRACK_NO_FILES_TO_DOWNLOAD = "EasyTrackImportHandler.NoFilesToDownloaded";
	public static final String EASY_TRACK_FTP_ERROR = "EasyTrackImportHandler.FTPError";
	public static final String EASY_TRACK_FTP_CONNECTION_FAILURE = "EasyTrackImportHandler.FTPConnectionFailure";
	
	public static final String LOGICWAY_SBB_CALCULATION_DRIVER_IS_ABSENT = "LogicWaySbb.SalaryCalculation.AbsentDriver";
	public static final String LOGICWAY_SBB_CALCULATION_SALARY_PERIOD_IS_ABSENT = "LogicWaySbb.SalaryCalculation.AbsentSalaryPeriod";
	public static final String LOGICWAY_SBB_CALCULATION_INTERNAL_ERROR = "LogicWaySbb.SalaryCalculation.InternalError";
	public static final String LOGICWAY_SBB_CALCULATION_STOPPING_CALCULATION = "LogicWaySbb.SalaryCalculation.StoppingCalculation";
	
}
