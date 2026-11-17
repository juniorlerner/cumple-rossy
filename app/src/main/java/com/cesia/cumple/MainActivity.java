package com.cesia.cumple;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {

    private static final int ROSA = Color.rgb(214, 51, 132);
    private static final int ROSA_OSCURO = Color.rgb(173, 20, 87);
    private static final int FONDO_SUAVE = Color.rgb(255, 245, 248);
    private static final int TEXTO = Color.rgb(90, 25, 50);
    private static final int ROSA_TEXTO_SUAVE = Color.rgb(190, 120, 150);

    private static final String CARTA =
            "Feliz cumpleaños, mi amor\n\n" +
            "Aunque hoy no pueda estar contigo para darte un abrazo, quería escribirte estas palabras " +
            "para que supieras lo importante que eres para mí. Desde que llegaste a mi vida, muchas cosas " +
            "cambiaron para bien. Me has regalado momentos muy bonitos, risas, confianza y recuerdos que " +
            "guardo con mucho cariño. Eres una personita muy increíble y me siento afortunado de poder " +
            "compartir parte de mi vida contigo.\n\n" +
            "Me da tristeza no poder acompañarte hoy porque pues soy yo quien está lejos, pero aun así " +
            "quería que sintieras que estoy contigo de alguna manera. Espero que disfrutes muchísimo " +
            "este día, que te consientan mucho y que recibas todo el cariño que te mereces. " +
            "Te extraño y tengo muchas ganas de volver a verte 💗";

    private static final String[] DESEOS = {
            "Salud, hoy y siempre",
            "Alegría todos los días",
            "Que se cumplan tus sueños",
            "Y yo a tu lado, en cada cumpleaños"
    };

    private final List<Animator> animaciones = new ArrayList<>();
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Random random = new Random();
    private Runnable maquinaRunnable;
    private Typeface fuenteScript;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            fuenteScript = Typeface.createFromAsset(getAssets(), "fonts/great_vibes.ttf");
        } catch (Exception e) {
            fuenteScript = Typeface.SERIF;
        }
        pantallaPortada();
    }

    @Override
    protected void onDestroy() {
        pararTodo();
        super.onDestroy();
    }

    private void pararTodo() {
        for (Animator a : animaciones) a.cancel();
        animaciones.clear();
        handler.removeCallbacksAndMessages(null);
        maquinaRunnable = null;
    }

    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density + 0.5f);
    }

    private GradientDrawable fondo(int color, float radioDp) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp((int) radioDp));
        return g;
    }

    private TextView texto(String s, float sp, int color, boolean negrita) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(sp);
        t.setTextColor(color);
        if (negrita) t.setTypeface(Typeface.DEFAULT_BOLD);
        return t;
    }

    private TextView textoScript(String s, float sp, int color) {
        TextView t = new TextView(this);
        t.setText(s);
        t.setTextSize(sp);
        t.setTextColor(color);
        t.setTypeface(fuenteScript != null ? fuenteScript : Typeface.SERIF);
        return t;
    }

    private GradientDrawable tarjetaBlanca() {
        GradientDrawable g = new GradientDrawable();
        g.setColor(Color.WHITE);
        g.setCornerRadius(dp(22));
        g.setStroke(dp(1), Color.rgb(231, 198, 106));
        return g;
    }

    private LinearLayout divisorDorado() {
        LinearLayout fila = new LinearLayout(this);
        fila.setOrientation(LinearLayout.HORIZONTAL);
        fila.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lpF = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpF.topMargin = dp(2);
        fila.setLayoutParams(lpF);
        int oro = Color.rgb(240, 200, 100);
        View l1 = new View(this);
        l1.setBackgroundColor(oro);
        fila.addView(l1, new LinearLayout.LayoutParams(dp(56), dp(2)));
        TextView cor = texto("💖", 15, Color.WHITE, false);
        cor.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lpC = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpC.leftMargin = dp(8);
        lpC.rightMargin = dp(8);
        cor.setLayoutParams(lpC);
        fila.addView(cor);
        View l2 = new View(this);
        l2.setBackgroundColor(oro);
        fila.addView(l2, new LinearLayout.LayoutParams(dp(56), dp(2)));
        return fila;
    }

    private LinearLayout puntos(int activo, boolean sobreFoto) {
        LinearLayout fila = new LinearLayout(this);
        fila.setOrientation(LinearLayout.HORIZONTAL);
        fila.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lpF = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpF.topMargin = dp(18);
        fila.setLayoutParams(lpF);
        for (int i = 0; i < 5; i++) {
            View d = new View(this);
            GradientDrawable g = new GradientDrawable();
            g.setShape(GradientDrawable.OVAL);
            LinearLayout.LayoutParams lp;
            if (i == activo) {
                g.setColor(sobreFoto ? Color.WHITE : ROSA);
                lp = new LinearLayout.LayoutParams(dp(10), dp(10));
            } else {
                g.setColor(sobreFoto ? Color.argb(110, 255, 255, 255) : Color.rgb(245, 200, 218));
                lp = new LinearLayout.LayoutParams(dp(7), dp(7));
            }
            lp.leftMargin = dp(4);
            lp.rightMargin = dp(4);
            d.setBackground(g);
            d.setLayoutParams(lp);
            fila.addView(d);
        }
        return fila;
    }

    private void agregarPuntos(FrameLayout raiz, int activo) {
        LinearLayout fila = puntos(activo, true);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        lp.bottomMargin = dp(14);
        raiz.addView(fila, lp);
    }

    private Button botonRosa(String s, float sp) {
        Button b = new Button(this);
        b.setText(s);
        b.setTextSize(sp);
        b.setTypeface(Typeface.DEFAULT_BOLD);
        b.setTextColor(Color.WHITE);
        GradientDrawable gb = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
                new int[]{Color.rgb(240, 80, 150), Color.rgb(199, 30, 100)});
        gb.setCornerRadius(dp(28));
        gb.setStroke(dp(1), Color.argb(130, 255, 215, 150));
        b.setBackground(gb);
        b.setAllCaps(false);
        b.setElevation(dp(4));
        return b;
    }

    private void latir(View v) {
        PropertyValuesHolder sx = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f, 1.15f);
        PropertyValuesHolder sy = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f, 1.15f);
        ObjectAnimator a = ObjectAnimator.ofPropertyValuesHolder(v, sx, sy);
        a.setDuration(600);
        a.setRepeatCount(ObjectAnimator.INFINITE);
        a.setRepeatMode(ObjectAnimator.REVERSE);
        a.setInterpolator(new AccelerateDecelerateInterpolator());
        a.start();
        animaciones.add(a);
    }

    private void flotantes(FrameLayout raiz, int ancho, int alto, int cantidad, String[] simbolos) {
        for (int i = 0; i < cantidad; i++) {
            TextView c = new TextView(this);
            c.setText(simbolos[random.nextInt(simbolos.length)]);
            c.setTextSize(16 + random.nextInt(18));
            int x = random.nextInt(Math.max(1, ancho - dp(50)));
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = x;
            lp.topMargin = alto;
            raiz.addView(c, lp);
            ObjectAnimator subir = ObjectAnimator.ofFloat(c, View.TRANSLATION_Y, 0, -(alto + dp(120)));
            subir.setDuration(6500 + random.nextInt(5000));
            subir.setStartDelay(random.nextInt(6000));
            subir.setRepeatCount(ObjectAnimator.INFINITE);
            subir.setInterpolator(new android.view.animation.LinearInterpolator());
            subir.start();
            animaciones.add(subir);
            ObjectAnimator girar = ObjectAnimator.ofFloat(c, View.ROTATION, -18, 18);
            girar.setDuration(1400 + random.nextInt(900));
            girar.setRepeatCount(ObjectAnimator.INFINITE);
            girar.setRepeatMode(ObjectAnimator.REVERSE);
            girar.start();
            animaciones.add(girar);
        }
    }

    private void corazonesFlotantes(FrameLayout raiz, int ancho, int alto, int cantidad) {
        flotantes(raiz, ancho, alto, cantidad, new String[]{"💗", "💖", "❤️", "🌸", "💕"});
    }

    private void confetiCayendo(FrameLayout raiz, int ancho, int alto, int cantidad) {
        flotantes(raiz, ancho, alto, cantidad, new String[]{"🎉", "✨", "🎊", "💖", "🎈", "⭐"});
    }

    private void corazonEn(FrameLayout raiz, float x, float y) {
        String[] sims = {"💗", "💖", "❤️", "💕"};
        TextView c = new TextView(this);
        c.setText(sims[random.nextInt(sims.length)]);
        c.setTextSize(20 + random.nextInt(10));
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = (int) x - dp(14);
        lp.topMargin = (int) y - dp(14);
        c.setLayoutParams(lp);
        raiz.addView(c);
        ObjectAnimator sube = ObjectAnimator.ofFloat(c, "translationY", 0, -dp(110));
        ObjectAnimator desvanece = ObjectAnimator.ofFloat(c, "alpha", 1f, 0f);
        sube.setDuration(950);
        desvanece.setDuration(950);
        sube.addListener(new AnimatorListenerAdapter() {
            @Override public void onAnimationEnd(Animator a) { raiz.removeView(c); }
        });
        sube.start();
        desvanece.start();
    }

    private void explosionConfeti(FrameLayout raiz) {
        int cx = raiz.getWidth() / 2;
        int cy = (int) (raiz.getHeight() * 0.32);
        String[] sims = {"🎉", "✨", "🎊", "💖", "🎈"};
        for (int i = 0; i < 22; i++) {
            TextView c = new TextView(this);
            c.setText(sims[random.nextInt(sims.length)]);
            c.setTextSize(18 + random.nextInt(16));
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = cx - dp(16);
            lp.topMargin = cy - dp(16);
            c.setLayoutParams(lp);
            raiz.addView(c);
            double ang = random.nextDouble() * Math.PI * 2;
            double dist = dp(90 + random.nextInt(150));
            ObjectAnimator mx = ObjectAnimator.ofFloat(c, "translationX", 0, (float) (Math.cos(ang) * dist));
            ObjectAnimator my = ObjectAnimator.ofFloat(c, "translationY", 0, (float) (Math.sin(ang) * dist) - dp(40));
            ObjectAnimator fade = ObjectAnimator.ofFloat(c, "alpha", 1f, 0f);
            mx.setDuration(1300); my.setDuration(1300); fade.setDuration(1300);
            mx.addListener(new AnimatorListenerAdapter() {
                @Override public void onAnimationEnd(Animator a) { raiz.removeView(c); }
            });
            mx.start(); my.start(); fade.start();
        }
    }

    private ImageView fondoImagen() {
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.fondo);
        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return img;
    }

    private View velo(int alphaAbajo, boolean todo) {
        View v = new View(this);
        GradientDrawable gv = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                new int[]{Color.argb(alphaAbajo, 70, 5, 30),
                        todo ? Color.argb(alphaAbajo / 3, 70, 5, 30) : Color.TRANSPARENT});
        v.setBackground(gv);
        return v;
    }

    // ===== Pantalla 1: Portada =====

    private void pantallaPortada() {
        pararTodo();
        FrameLayout raiz = new FrameLayout(this);
        raiz.addView(fondoImagen(), new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        View veloAbajo = velo(165, false);
        FrameLayout.LayoutParams lpVelo = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, dp(360));
        lpVelo.gravity = Gravity.BOTTOM;
        raiz.addView(veloAbajo, lpVelo);

        TextView musica = texto("♪  Cumpleaños feliz", 15, Color.rgb(255, 235, 240), false);
        musica.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams lpMus = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lpMus.topMargin = dp(28);
        raiz.addView(musica, lpMus);

        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setGravity(Gravity.CENTER_HORIZONTAL);
        col.setPadding(dp(24), dp(24), dp(24), dp(40));
        FrameLayout.LayoutParams lpCol = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        lpCol.gravity = Gravity.BOTTOM;
        raiz.addView(col, lpCol);

        TextView titulo = textoScript("¡Feliz Cumpleaños!", 52, Color.WHITE);
        titulo.setShadowLayer(8, 0, 2, Color.argb(160, 0, 0, 0));
        titulo.setGravity(Gravity.CENTER);
        col.addView(titulo);

        TextView nombre = textoScript("Rossy", 76, Color.WHITE);
        nombre.setShadowLayer(8, 0, 2, Color.argb(160, 0, 0, 0));
        nombre.setGravity(Gravity.CENTER);
        col.addView(nombre);
        col.addView(divisorDorado());

        Button btn = botonRosa("Abrir tu sorpresa", 18);
        LinearLayout.LayoutParams lpBtn = new LinearLayout.LayoutParams(dp(290), dp(58));
        lpBtn.topMargin = dp(22);
        btn.setLayoutParams(lpBtn);
        btn.setOnClickListener(v -> pantallaTorta());
        col.addView(btn);

        TextView pie = texto("Hecho con todo mi amor", 13, Color.rgb(255, 205, 220), false);
        pie.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lpPie = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpPie.topMargin = dp(14);
        pie.setLayoutParams(lpPie);
        col.addView(pie);

        setContentView(raiz);
        raiz.post(() -> {
            confetiCayendo(raiz, raiz.getWidth(), raiz.getHeight(), 10);
            corazonesFlotantes(raiz, raiz.getWidth(), raiz.getHeight(), 6);
        });
        raiz.setOnTouchListener((v, ev) -> {
            if (ev.getAction() == MotionEvent.ACTION_DOWN || ev.getAction() == MotionEvent.ACTION_MOVE) {
                corazonEn(raiz, ev.getX(), ev.getY());
            }
            return false;
        });
        agregarPuntos(raiz, 0);
    }

    // ===== Pantalla 2: La torta (toca las velitas para apagarlas) =====

    private void pantallaTorta() {
        pararTodo();
        FrameLayout raiz = new FrameLayout(this);
        GradientDrawable gf = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.rgb(252, 228, 236), Color.rgb(255, 245, 248)});
        raiz.setBackground(gf);

        ScrollView scroll = new ScrollView(this);
        raiz.addView(scroll, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setGravity(Gravity.CENTER_HORIZONTAL);
        col.setPadding(dp(24), dp(36), dp(24), dp(40));
        scroll.addView(col);

        TextView tit = textoScript("Pide un deseo...", 44, ROSA_OSCURO);
        tit.setGravity(Gravity.CENTER);
        col.addView(tit);

        // Velitas (5): llama arriba, vela blanca abajo
        LinearLayout filaVelas = new LinearLayout(this);
        filaVelas.setOrientation(LinearLayout.HORIZONTAL);
        filaVelas.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lpVelas = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpVelas.topMargin = dp(26);
        filaVelas.setLayoutParams(lpVelas);
        col.addView(filaVelas);

        final int[] apagadas = {0};
        final TextView deseo = textoScript("¡Tu deseo se hará realidad!", 34, ROSA_OSCURO);
        deseo.setGravity(Gravity.CENTER);
        deseo.setAlpha(0f);
        final Button btnSeguir = botonRosa("Seguir ✨", 17);
        btnSeguir.setAlpha(0f);
        btnSeguir.setEnabled(false);
        btnSeguir.setOnClickListener(v -> pantallaCarta());

        for (int i = 0; i < 5; i++) {
            LinearLayout vela = new LinearLayout(this);
            vela.setOrientation(LinearLayout.VERTICAL);
            vela.setGravity(Gravity.CENTER_HORIZONTAL);
            LinearLayout.LayoutParams lpV = new LinearLayout.LayoutParams(
                    dp(48), LinearLayout.LayoutParams.WRAP_CONTENT);
            vela.setLayoutParams(lpV);

            TextView llama = texto("🔥", 22, ROSA, false);
            llama.setGravity(Gravity.CENTER);
            vela.addView(llama);

            View palito = new View(this);
            palito.setBackground(fondo(Color.WHITE, 5));
            LinearLayout.LayoutParams lpP = new LinearLayout.LayoutParams(dp(10), dp(52));
            lpP.topMargin = dp(2);
            palito.setLayoutParams(lpP);
            vela.addView(palito);

            TextView humo = texto("💨", 16, ROSA, false);
            humo.setGravity(Gravity.CENTER);
            humo.setAlpha(0f);
            vela.addView(humo);

            vela.setOnClickListener(v -> {
                if (llama.getVisibility() == View.VISIBLE) {
                    llama.setVisibility(View.INVISIBLE);
                    humo.setAlpha(1f);
                    humo.animate().alpha(0f).setDuration(1200).start();
                    apagadas[0]++;
                    if (apagadas[0] == 5) {
                        deseo.animate().alpha(1f).setDuration(600).start();
                        btnSeguir.animate().alpha(1f).setDuration(600).start();
                        btnSeguir.setEnabled(true);
                        raiz.post(() -> explosionConfeti(raiz));
                    }
                }
            });
            filaVelas.addView(vela);
        }

        // Torta: 3 capas + plato
        View capaArriba = new View(this);
        capaArriba.setBackground(fondo(Color.rgb(252, 200, 220), 12));
        LinearLayout.LayoutParams lpA = new LinearLayout.LayoutParams(dp(148), dp(82));
        lpA.topMargin = dp(4);
        capaArriba.setLayoutParams(lpA);
        col.addView(capaArriba);

        View capaMedio = new View(this);
        capaMedio.setBackground(fondo(Color.rgb(240, 110, 165), 12));
        LinearLayout.LayoutParams lpM = new LinearLayout.LayoutParams(dp(196), dp(88));
        lpM.topMargin = -dp(4);
        capaMedio.setLayoutParams(lpM);
        col.addView(capaMedio);

        View capaAbajo = new View(this);
        capaAbajo.setBackground(fondo(Color.rgb(199, 30, 100), 12));
        LinearLayout.LayoutParams lpB = new LinearLayout.LayoutParams(dp(240), dp(110));
        lpB.topMargin = -dp(4);
        capaAbajo.setLayoutParams(lpB);
        col.addView(capaAbajo);

        View plato = new View(this);
        GradientDrawable gp = new GradientDrawable();
        gp.setShape(GradientDrawable.OVAL);
        gp.setColor(Color.rgb(235, 200, 160));
        plato.setBackground(gp);
        LinearLayout.LayoutParams lpPl = new LinearLayout.LayoutParams(dp(300), dp(30));
        lpPl.topMargin = -dp(6);
        plato.setLayoutParams(lpPl);
        col.addView(plato);

        TextView ayuda = texto("Toca las velitas para apagarlas", 16, TEXTO, false);
        ayuda.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lpAy = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpAy.topMargin = dp(28);
        ayuda.setLayoutParams(lpAy);
        col.addView(ayuda);

        LinearLayout.LayoutParams lpD = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpD.topMargin = dp(10);
        deseo.setLayoutParams(lpD);
        col.addView(deseo);

        LinearLayout.LayoutParams lpBtn = new LinearLayout.LayoutParams(dp(260), dp(56));
        lpBtn.topMargin = dp(18);
        lpBtn.gravity = Gravity.CENTER_HORIZONTAL;
        btnSeguir.setLayoutParams(lpBtn);
        col.addView(btnSeguir);

        col.addView(puntos(1, false));

        setContentView(raiz);
        raiz.post(() -> confetiCayendo(raiz, raiz.getWidth(), raiz.getHeight(), 8));
    }

    // ===== Pantalla 3: La carta (efecto máquina de escribir) =====

    private void pantallaCarta() {
        pararTodo();
        ScrollView scroll = new ScrollView(this);
        GradientDrawable gf = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.rgb(252, 228, 236), Color.rgb(255, 245, 248)});
        scroll.setBackground(gf);

        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setGravity(Gravity.CENTER_HORIZONTAL);
        col.setPadding(dp(22), dp(30), dp(22), dp(30));
        scroll.addView(col);

        TextView corazon = texto("💖", 50, ROSA, false);
        corazon.setGravity(Gravity.CENTER);
        col.addView(corazon);
        latir(corazon);

        TextView tit = textoScript("Para Rossy", 40, ROSA_OSCURO);
        tit.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lpTit = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpTit.topMargin = dp(6);
        tit.setLayoutParams(lpTit);
        col.addView(tit);

        LinearLayout tarjeta = new LinearLayout(this);
        tarjeta.setOrientation(LinearLayout.VERTICAL);
        tarjeta.setBackground(tarjetaBlanca());
        tarjeta.setElevation(dp(4));
        tarjeta.setPadding(dp(22), dp(22), dp(22), dp(22));
        LinearLayout.LayoutParams lpTar = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpTar.topMargin = dp(20);
        tarjeta.setLayoutParams(lpTar);
        col.addView(tarjeta);

        TextView carta = texto("", 17, TEXTO, false);
        carta.setLineSpacing(0, 1.25f);
        tarjeta.addView(carta);

        TextView firma = textoScript("Con amor, para ti", 26, ROSA_OSCURO);
        firma.setAlpha(0f);
        LinearLayout.LayoutParams lpFirma = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpFirma.topMargin = dp(16);
        firma.setLayoutParams(lpFirma);
        tarjeta.addView(firma);

        Button btn = botonRosa("Seguir ✨", 17);
        btn.setAlpha(0f);
        btn.setEnabled(false);
        LinearLayout.LayoutParams lpBtn = new LinearLayout.LayoutParams(dp(260), dp(56));
        lpBtn.topMargin = dp(24);
        btn.setLayoutParams(lpBtn);
        btn.setOnClickListener(v -> pantallaDeseos());
        col.addView(btn);

        final int[] pos = {0};
        maquinaRunnable = new Runnable() {
            @Override
            public void run() {
                if (pos[0] <= CARTA.length()) {
                    carta.setText(CARTA.substring(0, pos[0]));
                    pos[0]++;
                    handler.postDelayed(this, 26);
                } else {
                    firma.animate().alpha(1f).setDuration(600).start();
                    btn.animate().alpha(1f).setDuration(600).start();
                    btn.setEnabled(true);
                }
            }
        };
        handler.postDelayed(maquinaRunnable, 350);
        tarjeta.setOnClickListener(v -> {
            handler.removeCallbacks(maquinaRunnable);
            carta.setText(CARTA);
            firma.setAlpha(1f);
            btn.setAlpha(1f);
            btn.setEnabled(true);
        });
        col.addView(puntos(2, false));

        setContentView(scroll);
    }

    // ===== Pantalla 4: Mis deseos para ti =====

    private void pantallaDeseos() {
        pararTodo();
        ScrollView scroll = new ScrollView(this);
        scroll.setBackgroundColor(FONDO_SUAVE);

        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setPadding(dp(20), dp(30), dp(20), dp(28));
        scroll.addView(col);

        TextView tit = textoScript("Mis deseos para ti", 38, ROSA_OSCURO);
        tit.setGravity(Gravity.CENTER);
        col.addView(tit);

        for (int i = 0; i < DESEOS.length; i++) {
            LinearLayout tarjeta = new LinearLayout(this);
            tarjeta.setOrientation(LinearLayout.HORIZONTAL);
            tarjeta.setGravity(Gravity.CENTER_VERTICAL);
            tarjeta.setBackground(tarjetaBlanca());
            tarjeta.setElevation(dp(3));
            tarjeta.setPadding(dp(18), dp(16), dp(18), dp(16));
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.topMargin = dp(12);
            tarjeta.setLayoutParams(lp);
            tarjeta.setAlpha(0f);
            tarjeta.setTranslationY(dp(24));

            TextView cor = texto("💖", 22, ROSA, false);
            tarjeta.addView(cor);
            TextView txt = texto(DESEOS[i], 16, TEXTO, false);
            LinearLayout.LayoutParams lpT = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lpT.leftMargin = dp(12);
            txt.setLayoutParams(lpT);
            tarjeta.addView(txt);

            col.addView(tarjeta);
            tarjeta.animate().alpha(1f).translationY(0).setDuration(450)
                    .setStartDelay(200 + i * 350).start();
        }

        Button btn = botonRosa("Seguir ✨", 17);
        LinearLayout.LayoutParams lpBtn = new LinearLayout.LayoutParams(dp(260), dp(56));
        lpBtn.topMargin = dp(26);
        lpBtn.gravity = Gravity.CENTER_HORIZONTAL;
        btn.setLayoutParams(lpBtn);
        btn.setOnClickListener(v -> pantallaFinal());
        col.addView(btn);
        col.addView(puntos(3, false));

        setContentView(scroll);
    }

    // ===== Pantalla 5: Final =====

    private void pantallaFinal() {
        pararTodo();
        FrameLayout raiz = new FrameLayout(this);
        raiz.addView(fondoImagen(), new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        raiz.addView(velo(160, true), new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        LinearLayout col = new LinearLayout(this);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setGravity(Gravity.CENTER);
        col.setPadding(dp(24), dp(24), dp(24), dp(24));
        raiz.addView(col, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));

        TextView corazon = texto("❤️", 90, ROSA, false);
        corazon.setGravity(Gravity.CENTER);
        corazon.setShadowLayer(10, 0, 2, Color.argb(120, 0, 0, 0));
        col.addView(corazon);
        latir(corazon);

        TextView grande = textoScript("¡Feliz Cumpleaños!", 52, Color.WHITE);
        grande.setGravity(Gravity.CENTER);
        grande.setShadowLayer(8, 0, 2, Color.argb(170, 0, 0, 0));
        LinearLayout.LayoutParams lpG = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpG.topMargin = dp(10);
        grande.setLayoutParams(lpG);
        col.addView(grande);

        TextView nombre = textoScript("Rossy", 64, Color.WHITE);
        nombre.setGravity(Gravity.CENTER);
        nombre.setShadowLayer(8, 0, 2, Color.argb(170, 0, 0, 0));
        col.addView(nombre);

        TextView sub = texto("Que cumplas muchos más...\ny que yo esté en todos 💖", 18, Color.rgb(255, 230, 238), false);
        sub.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams lpSub = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lpSub.topMargin = dp(14);
        sub.setLayoutParams(lpSub);
        col.addView(sub);

        Button btn = botonRosa("Volver a ver", 16);
        LinearLayout.LayoutParams lpBtn = new LinearLayout.LayoutParams(dp(260), dp(54));
        lpBtn.topMargin = dp(24);
        btn.setLayoutParams(lpBtn);
        btn.setOnClickListener(v -> pantallaPortada());
        col.addView(btn);

        setContentView(raiz);
        agregarPuntos(raiz, 4);
        raiz.post(() -> {
            confetiCayendo(raiz, raiz.getWidth(), raiz.getHeight(), 12);
            corazonesFlotantes(raiz, raiz.getWidth(), raiz.getHeight(), 8);
            explosionConfeti(raiz);
        });
        raiz.setOnTouchListener((v, ev) -> {
            if (ev.getAction() == MotionEvent.ACTION_DOWN || ev.getAction() == MotionEvent.ACTION_MOVE) {
                corazonEn(raiz, ev.getX(), ev.getY());
            }
            return false;
        });
    }
}
