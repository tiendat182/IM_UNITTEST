package com.viettel.fw.bean;

import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import nl.captcha.text.producer.DefaultTextProducer;
import nl.captcha.text.renderer.DefaultWordRenderer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by thanhnt77 on 9/10/2015.
 */
public class SimpleCaptchaServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static int _width = 200;
    private static int _height = 50;

    public SimpleCaptchaServlet() {
    }

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (this.getInitParameter("captcha-height") != null) {
            _height = Integer.valueOf(this.getInitParameter("captcha-height"));
        }

        if (this.getInitParameter("captcha-width") != null) {
            _width = Integer.valueOf(this.getInitParameter("captcha-width"));
        }

    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        java.util.List<Color> textColors = Arrays.asList(
                Color.BLACK);
        java.util.List<Font> textFonts = Arrays.asList(
                new Font("Arial", Font.BOLD, 48),
                new Font("Courier", Font.BOLD, 48));

        Color backgroundColor = Color.LIGHT_GRAY;
        Captcha captcha = (new Captcha.Builder(_width, _height))
                .addText(
                        new DefaultTextProducer(),
                        new DefaultWordRenderer(textColors, textFonts))

                .build();
        CaptchaServletUtil.writeImage(resp, captcha.getImage());
        req.getSession().setAttribute("simpleCaptcha", captcha);
    }
}