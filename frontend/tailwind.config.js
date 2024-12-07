import daisyui from "daisyui";

export default {
  content: ["./index.html", "./src/**/*.{vue,js,ts,jsx,tsx}"],
  theme: {
    extend: {
      colors: {
        customBlue: '#1446a0',
        customPink: '#db3069',
        customYellow: '#f5d547',
        customBeige: '#ebebd3',
        customBg : '#f5f5f5'
      }
    }
  },
  plugins: [daisyui],
};