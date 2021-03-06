package appconcept.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

	//Employee not found for retrieval or update
	@ExceptionHandler			
	ResponseEntity<EmployeeErrorResponse> handleException(EmployeeNotFoundException exc) {
		EmployeeErrorResponse response = new EmployeeErrorResponse(HttpStatus.NOT_FOUND.value(), exc.getMessage(),System.currentTimeMillis());
		return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	}
	
	//Team not found for retrieval or update
	@ExceptionHandler			
	public ResponseEntity<TeamErrorResponse> handleException(TeamNotFoundException exc) {
		TeamErrorResponse response = new TeamErrorResponse(HttpStatus.NOT_FOUND.value(),exc.getMessage(),System.currentTimeMillis());
		return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
	}
	
	//catch-all general exception
	@ExceptionHandler
	public ResponseEntity<GeneralError> handleException(Exception exc) {
		GeneralError response = new GeneralError(HttpStatus.BAD_REQUEST.value(),exc.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
	
	//inner class example, could be handled by separate standalone Java class
	class GeneralError {
		private int status;
		private String message;
		private long timeStamp;
		
		public GeneralError() {}

		public GeneralError(int status, String message, long timeStamp) {
			this.status = status;
			this.message = message;
			this.timeStamp = timeStamp;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

		public long getTimeStamp() {
			return timeStamp;
		}

		public void setTimeStamp(long timeStamp) {
			this.timeStamp = timeStamp;
		}
		
	}
	
}
