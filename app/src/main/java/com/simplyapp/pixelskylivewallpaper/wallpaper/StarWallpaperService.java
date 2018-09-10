package com.simplyapp.pixelskylivewallpaper.wallpaper;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;

import com.simplyapp.pixelskylivewallpaper.utils.Colour;
import com.simplyapp.pixelskylivewallpaper.utils.StarsPrefsHelper;

public class StarWallpaperService extends android.service.wallpaper.WallpaperService {
    public StarWallpaperService() {
    }

    @Override
    public Engine onCreateEngine() {
        return new GridEngine();
    }

    private class GridEngine extends Engine {
        private Long fps = 1000L / StarsPrefsHelper.Companion.getDEFAULT_SPEED();

        private final Handler handler = new Handler();
        private final Runnable drawRunner = new Runnable() {
            @Override
            public void run() {
                draw();
            }
        };

        private boolean visible = true;
        private AnimatedStarsDrawer animatedStarsDrawer;

        GridEngine() {
            animatedStarsDrawer = new AnimatedStarsDrawer();

            setupStars();
            handler.removeCallbacks(drawRunner);
            handler.post(drawRunner);
        }

        private void setupStars() {
            StarsPrefsHelper prefs = new StarsPrefsHelper(StarWallpaperService.this);
            fps = 1000L / prefs.getSpeedFps();
            Colour colour = prefs.getColour();
            if (colour.getCustomId() == null) {
                animatedStarsDrawer.initStars(colour.getColours(), prefs.getStarCount(), prefs.getMinSize(), prefs.getMaxSize());
            } else {
                int[] colours = prefs.getCustomColorList(colour.getCustomId());
                if (colours.length == 0) return;
                animatedStarsDrawer.initStars(colours, prefs.getStarCount(), prefs.getMinSize(), prefs.getMaxSize());
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            this.visible = visible;
            if (visible) {
                setupStars();
                handler.removeCallbacks(drawRunner);
                handler.post(drawRunner);
            } else {
                handler.removeCallbacks(drawRunner);
            }
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            this.visible = false;
            handler.removeCallbacks(drawRunner);
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            setupStars();
            animatedStarsDrawer.setCanvasSize(width, height);
        }

        @Override
        public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
            super.onSurfaceRedrawNeeded(holder);
            draw();
        }

        private void draw() {
            SurfaceHolder holder = getSurfaceHolder();
            Canvas canvas = null;
            try {
                canvas = holder.lockCanvas();
                if (canvas != null) {
                    animatedStarsDrawer.draw(canvas);
                }
            } finally {
                if (canvas != null) {
                    holder.unlockCanvasAndPost(canvas);
                }
            }

            handler.removeCallbacks(drawRunner);

            if (visible) {
                handler.postDelayed(drawRunner, fps);
            }
        }
    }
}