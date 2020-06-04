package com.sd.laborator.services

import com.sd.laborator.interfaces.HtmlPrettyPrinterInterface
import org.springframework.stereotype.Service
import java.lang.StringBuilder

@Service
class HtmlPrettyPrinterService: HtmlPrettyPrinterInterface {

    private var nextService: Any = Any()

    override fun setNextService(o: Any) {
        nextService = o
    }

    override fun getPrettyHtml(par: Any): String {
        val methods = nextService.javaClass.declaredMethods
        if(methods.isNotEmpty()){
            var methodToCall = methods[0]
            if(methods.size > 1) {
                for (method in methods) {
                    if(method.name.toLowerCase().startsWith("get")){
                        methodToCall = method
                        break
                    }
                }
            }
            var result = methodToCall.invoke(nextService, par)
            var html: StringBuilder = StringBuilder()
            html.append("<html>")
            html.append("<head></head>")
            html.append("<body>")
            html.append("<h1>${result.javaClass.simpleName}</h1>")
            var fields = result.javaClass.declaredFields
            html.append("<ul>")
            for(field in fields){
                if (!field.isAccessible){
                    field.isAccessible = true
                }
                var value = field.get(result)
                var isImage : Boolean = value.toString().endsWith("png") || value.toString().endsWith("jpg")
                var representation = if(isImage) "<img src=\"${value}\" height=\"35\">" else value.toString()
                html.append("<li>${field.name}: ${representation}</li>")
            }
            html.append("</ul>")
            html.append("</body>")
            html.append("</html>")
            return html.toString()
        }
        return "Invalid methods from next service"

    }

}