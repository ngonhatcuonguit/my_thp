package com.cuongngo.core_project.services.network.invoker

import java.io.IOException

class ApiException(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)