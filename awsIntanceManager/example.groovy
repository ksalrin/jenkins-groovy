String createAMI(ImageCreateRequest request, Context context) {

		LambdaLogger logger = context.getLogger();

		AmazonEC2Async client = createEC2Client();

		String imageId = null;
		try {
			Future<CreateImageResult> result = client
					.createImageAsync(new CreateImageRequest(request.getInstanceId(), request.getAmiName())
							.withNoReboot(request.isNoReboot()));
			while (!result.isDone()) {
				Thread.sleep(1000);
			}
			imageId = result.get().getImageId();

			logger.log("AMI Create Request End. instanceId[" + request.getInstanceId() + "] noReboot["
					+ request.isNoReboot() + "] imageId[" + imageId + "]");
		} catch (Exception e) {
			throw new RuntimeException("An unexpected error at the time of AMI creation has occurred", e);
		} finally {
			client.shutdown();
		}
		return imageId;
	}
